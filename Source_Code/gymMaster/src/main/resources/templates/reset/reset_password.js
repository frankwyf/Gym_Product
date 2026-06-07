const passwordField = document.getElementById("password");
const confirmPasswordField = document.getElementById("confirmpassword");
const passwordError = document.getElementById("password-error");
const confirmPasswordError = document.getElementById("confirmpassword-error");

function passwordValidation() {
    const password = passwordField.value;
    const confirmPassword = confirmPasswordField.value;
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,16}$/;

    if (!passwordRegex.test(password)) {
        passwordError.style.display = "block";
        passwordField.classList.add("is-invalid");
        return false;
    } else {
        passwordError.style.display = "none";
        passwordField.classList.remove("is-invalid");
    }

    if (password !== confirmPassword) {
        confirmPasswordError.style.display = "block";
        confirmPasswordField.classList.add("is-invalid");
        return false;
    } else {
        confirmPasswordError.style.display = "none";
        confirmPasswordField.classList.remove("is-invalid");
    }

    return true;
}

document.querySelector("form").addEventListener("submit", function(event) {
    if (!passwordValidation()) {
        event.preventDefault();
    }
});
function passwordVisibility() {
    const field = document.getElementById("password");
    const showPass = document.getElementById("showPass");
    const hidePass = document.getElementById("hidePass");

    hidePass.classList.remove("d-none");

    if (field.type === "password") {
        field.type = "text";
        showPass.style.display = "none";
        hidePass.style.display = "block";
    } else {
        field.type = "password";
        showPass.style.display = "block";
        hidePass.style.display = "none";
    }
}
function custom_close(){
    if (confirm("Please re-login through smartphone app !")){
        window.open('', '_self').close();
    }
    else{}
}