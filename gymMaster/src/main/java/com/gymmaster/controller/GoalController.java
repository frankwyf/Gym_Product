package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.common.CurrentUserResolver;
import com.gymmaster.entity.Goal;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Manages customer fitness goals (height, weight targets, weekly sessions, etc.).
 *
 * <p>Each customer may maintain multiple goals. All mutating operations are
 * ownership-checked: a customer can only modify their own goals.
 */
@Slf4j
@RestController
@RequestMapping("/goal")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;
    private final CurrentUserResolver currentUserResolver;

    /** Return all goals for the authenticated customer. */
    @GetMapping("/list")
    public BackMsg<List<Goal>> list(HttpServletRequest request) {
        int uid = currentUserResolver.getUserId(request);
        return BackMsg.success(goalService.list(
                new LambdaQueryWrapper<Goal>().eq(Goal::getUid, uid)));
    }

    /** Return a single goal by ID (must belong to the authenticated customer). */
    @GetMapping("/{gid}")
    public BackMsg<Goal> get(@PathVariable int gid, HttpServletRequest request) {
        int uid = currentUserResolver.getUserId(request);
        Goal goal = goalService.getById(gid);
        if (goal == null || goal.getUid() != uid) {
            throw new BusinessException("Goal not found or access denied.");
        }
        return BackMsg.success(goal);
    }

    /** Create a new goal for the authenticated customer. */
    @PostMapping("/add")
    public BackMsg<Goal> add(@Valid @RequestBody Goal goal, HttpServletRequest request) {
        int uid = currentUserResolver.getUserId(request);
        goal.setGid(null);  // prevent client-supplied ID
        goal.setUid(uid);   // always bind to the authenticated user
        goalService.save(goal);
        log.info("Goal created for customer uid={}: gid={}", uid, goal.getGid());
        return BackMsg.success(goal);
    }

    /** Update an existing goal (ownership verified). */
    @PutMapping("/edit")
    public BackMsg<String> edit(@Valid @RequestBody Goal goal, HttpServletRequest request) {
        int uid = currentUserResolver.getUserId(request);
        Goal existing = goalService.getById(goal.getGid());
        if (existing == null || existing.getUid() != uid) {
            throw new BusinessException("Goal not found or access denied.");
        }
        goal.setUid(uid);  // do not let clients change ownership
        goalService.update(goal,
                new LambdaQueryWrapper<Goal>().eq(Goal::getGid, goal.getGid()));
        return BackMsg.success("Goal updated.");
    }

    /** Delete a goal (ownership verified). */
    @DeleteMapping("/delete/{gid}")
    public BackMsg<String> delete(@PathVariable int gid, HttpServletRequest request) {
        int uid = currentUserResolver.getUserId(request);
        Goal existing = goalService.getById(gid);
        if (existing == null || existing.getUid() != uid) {
            throw new BusinessException("Goal not found or access denied.");
        }
        goalService.removeById(gid);
        log.info("Goal gid={} deleted by uid={}", gid, uid);
        return BackMsg.success("Goal deleted.");
    }
}
