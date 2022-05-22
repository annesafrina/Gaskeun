const suggestions = ["Aqua", "Samsung", "Asus", "Logitech"]

const searchFilterButton = document.getElementById("filter-button");

searchFilterButton.addEventListener("click", (e) => {
    e.preventDefault();
    const filterTab = document.querySelector(".custom-search__filters");
    filterTab.classList.toggle("open");
});


const searchInputs = document.querySelectorAll(".autocomplete-search input");

searchInputs.forEach(input => {
    input.addEventListener("keyup", (e) => {
        let query = e.target.value;
        let recommendations = []
        if (query) {
            recommendations = suggestions.filter(data => {
                return data.toLowerCase().startsWith(query.toLowerCase());
            })
            recommendations = recommendations.map(data => {
                return data = `<li>${data}</li>`;
            })
            input.nextElementSibling.classList.add("show")
        } else {
            input.nextElementSibling.classList.remove("show")
        }
        populateRecommendations(recommendations, input.nextElementSibling.firstElementChild);
    })
    input.addEventListener("blur", () => {
        setTimeout(() => input.nextElementSibling.classList.remove("show"), 150);
    })
});

function populateRecommendations(recommendations, recBox) {
    
    if (!recommendations.length) {
        recBox.innerHTML = `
            <li>no results</li>
        `;
    } else {
        let recs = recommendations.join('');
        recBox.innerHTML = recs;
        recBox.querySelectorAll("li").forEach(item => {
            item.setAttribute("onclick", "select(this)");
        });
    }
}

function select(item) {
    item.parentElement.parentElement.previousElementSibling.value = item.textContent;
}