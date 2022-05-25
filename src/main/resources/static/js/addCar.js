function changeUploadedFileName(e, fileNameDisplay) {
    const imageFileName = e.target.value.split("\\").at(-1);

    fileNameDisplay.innerText = imageFileName;
}

const fileNameText = document.querySelector("#file-name");
const input = document.querySelector(".form-input__file-hide");

input.addEventListener("change", (e) =>
    changeUploadedFileName(e, fileNameText)
);
