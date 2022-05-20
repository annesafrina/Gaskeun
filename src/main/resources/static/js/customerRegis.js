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

const registerStepButton = (stepButton, formSteps, currentStepInd, stepButtons) => {
    const eventListener = async (e) => {
        const target = e.target;

        
        const [currentStep] = currentStepInd;
        
        const value = Number(target.dataset.step) - 1;

        const currentStepBlob = stepButtons[currentStep];
        const nextStepBlob = stepButtons[value];
        
        currentStepBlob.classList.remove("step__circle--select");
        nextStepBlob.classList.add("step__circle--select");
        
        const currentForm = formSteps[currentStep];
        const nextForm = formSteps[value];

        currentForm.classList.remove("form-step--show");

        await sleep(400);
        nextForm.classList.add("form-step--show");

        currentStepInd[0] = value;

        

    }

    stepButton.addEventListener("click", eventListener);
}

function sleep(timeout) {
    return new Promise(resolve => setTimeout(resolve, timeout));
}

const password = document.querySelector("#password");
const passwordBtn = document.querySelector("#password-btn");

const confirmPassword = document.querySelector("#password-confirm");
const confirmPasswordBtn = document.querySelector("#password-confirm-btn");

addChangePasswordEventListener(password, passwordBtn);
addChangePasswordEventListener(confirmPassword, confirmPasswordBtn);


const continueBtn = document.querySelector("#continue");
const formSteps = document.querySelectorAll(".form-step");
const stepBtns = document.querySelectorAll(".step__circle");

const currentStepInd = [0]

stepBtns.forEach(btn => registerStepButton(btn, formSteps, currentStepInd, stepBtns));

const continueEvent = async (e) => {
    e.preventDefault();

    const [currentStep] = currentStepInd;
    value = currentStep + 1;

    const currentStepBlob = stepBtns[currentStep];
    const nextStepBlob = stepBtns[value];
    
    currentStepBlob.classList.remove("step__circle--select");
    nextStepBlob.classList.add("step__circle--select");
    
    const currentForm = formSteps[currentStep];
    const nextForm = formSteps[value];

    currentForm.classList.remove("form-step--show");

    await sleep(400);
    nextForm.classList.add("form-step--show");

    currentStepInd[0] = value;

}

continueBtn.addEventListener("click", continueEvent);