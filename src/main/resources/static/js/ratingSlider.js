const ratingSlider = document.getElementById("rating-slider");
let stars = ratingSlider.children;
let currentSelectedRating = 0;

ratingSlider.addEventListener("mouseleave", () => {
  if (currentSelectedRating === 0) {
    for (let star of stars) {
      star.style.color = "";
    }
  }
});

for (let i = 0; i < stars.length; i++) {
  stars[i].addEventListener("mouseenter", () => {
    currentSelectedRating = 0;
    for (let j = 0; j < i + 1; j++) {
      stars[j].style.color = "#83eaf1";
    }
    for (let k = i + 1; k < stars.length; k++) {
      stars[k].style.color = "";
    }
  });
  stars[i].addEventListener("click", () => {
    currentSelectedRating = i + 1;
    for (let j = 0; j < i + 1; j++) {
      stars[j].style.color = "#83eaf1";
    }
    for (let k = i + 1; k < stars.length; k++) {
      stars[k].style.color = "";
    }
  });

}




