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


function filterData(queryParams) {
    document.querySelector(".car-list").innerHTML = ""
    const loader = document.createElement("div")
    loader.classList.add("lds-dual-ring")
    document.body.appendChild(loader)
    fetch(`/explore/api/?${queryParams}`)
        .then(data => data.json())
        .then(json => {
            displayCarList(json)
            loader.remove()
        })
}

function displayCarList(json) {
    let carList = document.querySelector(".car-list")
    let currentRow = document.createElement("div")
    currentRow.classList.add("car-list__row")
    let rows = document.querySelectorAll(".car-list__row")
    if (rows.length) {
        let lastRow = rows[rows.length - 1]
        if (lastRow.children.length < 4) {
            currentRow = lastRow
        } else {
            carList.appendChild(currentRow)
        }
    } else {
        carList.appendChild(currentRow)
    }
    json.forEach((car, i) => {
        if (currentRow.children.length % 4 === 0) {
            currentRow = document.createElement("div")
            currentRow.classList.add("car-list__row")
            carList.appendChild(currentRow)
        }
        let card = document.createElement("div")
        card.classList.add("car-list__card")

        let availableDate = document.createElement("div")
        availableDate.classList.add("available-date")
        availableDate.innerText = `Available from ${dateParser(car.availableStartDate)}`

        let modelName = document.createElement("div")
        modelName.classList.add("model")
        modelName.innerText = car.model

        let capacity = document.createElement("div")
        capacity.classList.add("capacity")
        capacity.innerHTML = `
        <i class="fa-solid fa-user-group"></i>
        ${car.capacity}
        `

        let picture = document.createElement("div")
        picture.classList.add("picture")
        let image = new Image()
        console.log(car.picture)
        image.src = `data:image/png;base64,${car.picture}`
        picture.appendChild(image)

        let transmission = document.createElement("div")
        transmission.classList.add("transmission")
        transmission.innerHTML = `
        <i class="fa-solid fa-gears"></i>
        ${car.transmission}
        `

        let location = document.createElement("div")
        location.classList.add("location")
        location.innerHTML = `
        <i class="fa-solid fa-location-dot"></i>
        ${car.location.cityName}
        `

        let rating = document.createElement("div")
        rating.classList.add("stars")
        rating.innerHTML = `
        <i class="fa-solid fa-star"></i>
        ${parseFloat(car.rating)}
        `

        let rate = document.createElement("div")
        rate.classList.add("rate")
        rate.innerText = "Rp " + car.priceRate + " /day"

        card.appendChild(rating)
        card.appendChild(rate)
        card.appendChild(location)
        card.appendChild(transmission)
        card.appendChild(picture)
        card.appendChild(capacity)
        card.appendChild(modelName)
        card.appendChild(availableDate)
        currentRow.appendChild(card)
    })

    function dateParser(string) {
        const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]

        const parsedDate = new Date(Date.parse(string))

        return `${parsedDate.getDate()}, ${months[parsedDate.getMonth()]} ${parsedDate.getFullYear()}`
    }
}



function load() {
    const form = $(".custom-search")
    populateAvailableCarModels()
    populateAvailableLocations()
    filterData(form.serialize())
    document.querySelector(".custom-search").addEventListener("submit", (e) => {
        e.preventDefault();
        filterData(form.serialize())
        document.querySelector(".custom-search__filters").classList.remove("open")
    })
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
    fetch(`/api/all-locations`)
        .then(data => data.json())
        .then(json => {
            json.forEach(loc => {
                locationSuggestions.push(loc)
            })
        })
}