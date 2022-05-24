const carSuggestions = []
const locationSuggestions = []

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
        const suggestionsList = input.getAttribute("name") === "model" ? carSuggestions : locationSuggestions
        if (query) {
            recommendations = suggestionsList.filter(data => {
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


addEventListener("load", load)


function filterData() {
    const queryParams = new URLSearchParams(location.search)
    fetch(`/explore/api/${queryParams}`)
        .then(data => data.json())
        .then(json => {
            displayCarList(json)
        })
}

function displayCarList(json) {
    let carList = document.querySelector(".car-list")
    let currentRow = document.createElement("div")
    currentRow.classList.add("car-list__row")
    carList.appendChild(currentRow)
    json.forEach((car, i) => {
        if (i % 4 === 0) {
            currentRow = document.createElement("div")
            currentRow.classList.add("car-list__row")
            carList.appendChild(currentRow)
        }
        let card = document.createElement("div")
        card.classList.add("car-list__card")

        let availableDate = document.createElement("div")
        availableDate.classList.add("available-date")
        const parsedDate = new Date(Date.parse(car.availableStartDate))
        availableDate.innerText = ""

        card.appendChild(availableDate)
        currentRow.appendChild(card)
    })
}

function load() {
    populateAvailableCarModels()
    populateAvailableLocations()
    filterData()
}

function populateAvailableCarModels() {
    fetch(`/explore/api/model-names`)
        .then(data => data.json())
        .then(json => {
            json.forEach(car => {
                carSuggestions.push(car)
            })
        })
}


function populateAvailableLocations() {

}