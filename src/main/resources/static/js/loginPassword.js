const showPasswordButton = document.querySelector("#password-btn");
const passwordInput = document.querySelector("#password");

let showPassword = false;

const changePasswordType = (e) => {
    showPassword = !showPassword;
    if (showPassword) {
        passwordInput.type = "text";
        showPasswordButton.classList.remove("fa-eye");
        showPasswordButton.classList.add("fa-eye-slash");
        return;
    }

    passwordInput.type = "password";
    showPasswordButton.classList.remove("fa-eye-slash");
    showPasswordButton.classList.add("fa-eye");
}

showPasswordButton.addEventListener("click", changePasswordType);
