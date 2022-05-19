const addChangePasswordEventListener = (passwordInput, passwordButton) => {

    const eventListener = (e) => {
        const passwordShown = (passwordInput.type === "password") ? false : true;
        
        if (passwordShown) {
            passwordInput.type = "password";
            passwordButton.classList.remove("fa-eye-slash");
            passwordButton.classList.add("fa-eye");
            return;
        }

        passwordInput.type = "text";
        passwordButton.classList.remove("fa-eye");
        passwordButton.classList.add("fa-eye-slash");
    }

    passwordButton.addEventListener("click", eventListener);
}

const password = document.querySelector("#password");
const passwordBtn = document.querySelector("#password-btn");


const confirmPassword = document.querySelector("#password-confirm");
const confirmPasswordBtn = document.querySelector("#password-confirm-btn");

addChangePasswordEventListener(password, passwordBtn);
addChangePasswordEventListener(confirmPassword, confirmPasswordBtn);