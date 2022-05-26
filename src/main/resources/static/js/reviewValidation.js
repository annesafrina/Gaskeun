const form = document.querySelector(".review");

form.addEventListener("submit", (e) => {
  e.preventDefault();

  if (currentSelectedRating === 0) {
    const currentToast = document.getElementById("error-toast");
    if (currentToast) {
      currentToast.remove();
    }
    const toast = document.createElement("div");
    toast.id = "error-toast";
    toast.innerHTML = `
        <i class="fa-solid fa-exclamation"></i>
        <h1>Please enter a rating.</h1>
        <span class="close-error">&times;</span>
        `;
    document.body.insertBefore(toast, document.body.firstChild)

    toast.querySelector(".close-error").addEventListener("click", () => {
      toast.remove();
    });
  } else {
    form.submit();
  }
});
