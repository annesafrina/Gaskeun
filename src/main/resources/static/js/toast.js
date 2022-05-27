const toast = document.querySelector(".toast");
if (toast != null) {
    const closeBtn = document.querySelector(".toast .close-error");

    closeBtn.addEventListener("click", (e) => {
        toast.remove();
    });
}