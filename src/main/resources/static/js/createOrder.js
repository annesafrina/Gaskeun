const toast = document.querySelector(".toast");
if (toast != null) {
  const closeBtn = document.querySelector(".toast .close-error");

  closeBtn.addEventListener("click", (e) => {
    toast.remove();
  });
}

const priceSpan = document.querySelector(".price-show__price");
const hiddenInput = document.querySelector("#price-per-day");

const startDate = document.querySelector("#start-date");
const endDate = document.querySelector("#end-date");

const changePriceDisplay = (e) => {
  const startDateValue = startDate.value;
  const endDateValue = endDate.value;

  if (startDateValue == "" || endDateValue == "") return;

  const startDateDate = new Date(startDateValue);
  const endDateDate = new Date(endDateValue);

  const pricePerDay = Number(hiddenInput.value);
  const differentDays = Math.ceil(
      (endDateDate.getTime() - startDateDate.getTime()) / (1000 * 24 * 3600)
  );

  const totalPrice = pricePerDay * differentDays;
  priceSpan.innerText = Intl.NumberFormat("id-ID").format((totalPrice > 0) ? totalPrice : 0);
};

startDate.addEventListener("change", changePriceDisplay);
endDate.addEventListener("change", changePriceDisplay);
