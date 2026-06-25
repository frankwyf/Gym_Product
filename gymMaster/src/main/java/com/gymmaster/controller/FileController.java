package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.common.CurrentUserResolver;
import com.gymmaster.entity.Customer;
import com.gymmaster.entity.LoginUser;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.CustomerService;
import com.gymmaster.utils.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

/**
 * Handles profile picture upload/download and post media upload.
 *
 * <p>Security controls:
 * <ul>
 *   <li>Upload: extension whitelist (jpg/jpeg/png/gif/webp only)</li>
 *   <li>Download: filename validated against safe pattern to prevent path traversal</li>
 * </ul>
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS =
            Set.of("jpg", "jpeg", "png", "gif", "webp");

    // Safe filename: UUID hex chars + dot + extension, no path separators
    private static final java.util.regex.Pattern SAFE_FILENAME =
            java.util.regex.Pattern.compile("^[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9]{2,5}$");

    @Value("${gym.path}")
    private String basePath;

    private final RedisCache redisCache;
    private final CustomerService customerService;
    private final CurrentUserResolver currentUser;

    @PostMapping("/upload/customer")
    public BackMsg<String> upload(@RequestParam("file") MultipartFile file,
                                  HttpServletRequest request) throws IOException {
        String fileName = saveFile(file, "customerpro");

        // Update customer profile in DB and Redis
        int uid = currentUser.getUserId(request);
        String redisKey = "login" + uid;
        LoginUser user = redisCache.getCacheObject(redisKey);
        if (user == null) throw new BusinessException("Session expired — please log in again.");

        user.getCustomer().setProfile(fileName);
        LambdaQueryWrapper<Customer> qw = new LambdaQueryWrapper<Customer>()
                .eq(Customer::getUid, user.getCustomer().getUid());
        redisCache.setCacheObject(redisKey, user);
        customerService.update(user.getCustomer(), qw);
        return BackMsg.success(fileName);
    }

    @PostMapping("/upload/posts")
    public BackMsg<String> uploadPosts(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = saveFile(file, "posts");
        return BackMsg.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        if (name == null || !SAFE_FILENAME.matcher(name).matches()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String ext = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(ext)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        response.setContentType("image".equals(ext) || "png".equals(ext) ? "image/png" : "image/jpeg");
        Path filePath = Paths.get(basePath, "customerpro", name).normalize();
        // Extra guard: resolved path must stay inside basePath/customerpro/
        Path base = Paths.get(basePath, "customerpro").normalize();
        if (!filePath.startsWith(base)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try (InputStream in = Files.newInputStream(filePath);
             ServletOutputStream out = response.getOutputStream()) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            log.error("Failed to download file {}", name, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // --- helpers ---

    private String saveFile(MultipartFile file, String subDir) throws IOException {
        String original = file.getOriginalFilename();
        if (original == null || !original.contains(".")) {
            throw new BusinessException("Invalid file name.");
        }
        String ext = original.substring(original.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(ext)) {
            throw new BusinessException("Unsupported file type. Allowed: jpg, jpeg, png, gif, webp.");
        }
        String fileName = UUID.randomUUID() + "." + ext;
        File dir = new File(basePath + subDir + "/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Files.write(Paths.get(basePath + subDir + "/" + fileName), file.getBytes());
        return fileName;
    }
}
