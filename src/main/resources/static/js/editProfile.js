const editButton = document.getElementById("edit-profile");
let fields = [];
const type = document.title.substring(0,document.title.indexOf(" "))
const isProvider = type === "provider";
let infoChanged = false;

editButton.addEventListener("click", () => {
    showActions();
    showForm();
    editButton.remove();
})

function showActions() {
    const editConfirmButton = document.createElement("div");
    const editCancelButton = document.createElement("div");
    editConfirmButton.classList.add("edit");
    editCancelButton.classList.add("cancel");
    editConfirmButton.innerHTML = `&check;`
    editCancelButton.innerHTML = `&times;`
    const buttonContainer = document.getElementById("action-container");
    buttonContainer.appendChild(editCancelButton);
    buttonContainer.appendChild(editConfirmButton);
    editCancelButton.addEventListener("click", hideForm)
    editConfirmButton.addEventListener("click", () => {
        console.log("clicked");
        if (infoChanged) {
            document.getElementById("edits").submit()
        }
    })
    if (!infoChanged) {
        editConfirmButton.classList.add("disable")
    } else {
        editConfirmButton.classList.remove("disable")
    }

}

function showForm() {
    const form = document.createElement("form")
    form.innerHTML = `
    <input type="text" id="name" name="name">
    <input type="text" id="phoneNumber" name="phoneNumber">
    <label for="">Change Password (Optional)</label>
    <input type="password" id="oldPassword" name="oldPassword" placeholder="enter your old password">
    <input type="password" id="newPassword" name="password" placeholder="new password">
    <input type="password" id="password" name="passwordConfirmation" placeholder="confirm password">
    `
    form.id = "edits"
    const fieldElements = document.querySelectorAll(".editable-field");
    fields = []
    fieldElements.forEach(el => {
        fields.push(el.innerText);
        el.remove();
    })
    const inputs = form.querySelectorAll("input");
    inputs[0].value = fields[0].substring(fields[0].indexOf(":") + 1)
    inputs[1].value = fields[1].substring(fields[1].indexOf(":") + 1)
    form.setAttribute("method", "POST");
    form.setAttribute("action", `/${type}/edit`)
    inputs.forEach(el => el.addEventListener("keyup", checkChange))
    const email = document.getElementById("email");
    email.after(form);
}


function hideForm() {
    infoChanged = false;
    const nameField = document.createElement("div")
    const phoneField = document.createElement("div")
    nameField.classList.add("editable-field")
    phoneField.classList.add("editable-field")
    const form = document.getElementById("edits");

    form.remove();
    const email = document.getElementById("email");
    for (let i=fields.length-1; i >= 0; i--) {
        let field = fields[i]
        const fieldEl = document.createElement("div");
        fieldEl.classList.add("editable-field");
        fieldEl.innerText = field;
        email.after(fieldEl);
    }

    const confirmButton = document.querySelector(".edit")
    const cancelButton = document.querySelector(".cancel")
    confirmButton.remove();
    cancelButton.remove();
    const buttonContainer = document.getElementById("action-container");
    const editButton = document.createElement("div");
    editButton.id = "edit-profile";
    editButton.innerText = "Edit?"
    buttonContainer.appendChild(editButton);
    editButton.addEventListener("click", () => {
        showActions();
        showForm();
        editButton.remove();
    }) 
}

function checkChange() {
    const name = fields[0].substring(fields[0].indexOf(":") + 1);
    const phone = fields[1].substring(fields[1].indexOf(":") + 1);
    const form = document.getElementById("edits");
    const inputs = form.querySelectorAll("input");
    infoChanged = inputs[0].value !== name || inputs[1].value !== phone;
    const confirmButton = document.querySelector(".edit");
    const oldPassword = inputs[2];

    if (!infoChanged) {
        confirmButton.classList.add("disable")
    } else {
        confirmButton.classList.remove("disable")
    }
    if (oldPassword.value.length) {
        if (!(inputs[3].value.length && inputs[4].value.length)) {
            infoChanged = false
            confirmButton.classList.add("disable")
        } else {

            infoChanged = true
            confirmButton.classList.remove("disable")
        }
    }
}

const closeToast = document.querySelector(".close-error");

if (closeToast) {
    closeToast.addEventListener("click", () => {
        document.getElementById("error-toast").remove();
    })
}