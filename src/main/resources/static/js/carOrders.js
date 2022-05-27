function setUpFetch() {

    const allChildren = [...orders.children];
    allChildren.forEach(child => child.remove());

    const spinner = document.createElement("div");
    spinner.classList.add("lds-dual-ring");
    orders.appendChild(spinner);

    return spinner;
}

function afterFetch(spinner, data, type) {
    spinner.remove();

    if (data.length === 0) {
        const failCard = document.createElement("div");
        failCard.classList.add("order__div");

        failCard.innerHTML = `<div class="order__div">
          <h2 class="order__fail">Oops, you have no orders.</h2>
          <svg
            class="order__fail--svg"
            xmlns="http://www.w3.org/2000/svg"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            version="1.1"
            id="Capa_1"
            x="0px"
            y="0px"
            viewBox="0 0 47 47"
            style="enable-background: new 0 0 47 47"
            xml:space="preserve"
          >
            <g>
              <path
                style="fill: #ffd581"
                d="M44,19.662L44,19.662c-1.74-9.843-10.158-17-20.5-17S4.74,9.819,3,19.662l0,0   c-1.663,0.661-3,2.602-3,4.5c0,1.978,1.284,3.639,3.058,4.242C5.21,37.715,13.536,44.662,23.5,44.662   c9.965,0,18.29-6.948,20.442-16.258l0,0.001C45.716,27.801,47,26.14,47,24.162C47,22.264,45.663,20.323,44,19.662z"
              />
              <path
                style="fill: #cb8252"
                d="M31,39.663c-0.553,0-1-0.447-1-1c0-3.309-2.691-6-6-6s-6,2.691-6,6c0,0.553-0.447,1-1,1   s-1-0.447-1-1c0-4.411,3.589-8,8-8s8,3.589,8,8C32,39.216,31.553,39.663,31,39.663z"
              />
              <circle style="fill: #414141" cx="17" cy="25.662" r="2" />
              <circle style="fill: #414141" cx="31" cy="25.662" r="2" />
              <path
                style="fill: #414141"
                d="M44,19.662l0,0.001C42.26,9.821,33.684,2.338,23.342,2.338S4.74,9.819,3,19.662   c0,0,16.906,4.33,28.242-4.726c0.555-0.444,1.362-0.34,1.758,0.25C35.049,18.238,38.828,21.942,44,19.662z"
              />
            </g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
          </svg>
        </div>`;
        orders.appendChild(failCard);
        return;
    }

    data.forEach(object => {
        const { id, carName, status, startDate, endDate, customerReviewed, carReviewed, providerReviewed } = object;
        const divCard = document.createElement("div");
        const cssStyle = CSS_MAP[status];
        divCard.classList.add("order__card");
        divCard.innerHTML = `
        <div class="order__card--left">
            <div class="order__title">${carName}</div>
            <div class="order__circle order__text">#${id}</div>
            <div class="order__circle order__text" style="${cssStyle}">${status}</div>
            <div class="order__date">
              <i class="fa-solid fa-calendar"></i>
              <p>${startDate}</p>
              <p>-</p>
              <p>${endDate}</span>
            </div>
          </div>`;

        const carReviewButton = `<a href="/review/create/car/${id}" class="button button-green">Review Car</a>`;
        const providerReviewButton = `<a href="/review/create/provider/${id}" class="button button-green">Review Provider</a>`;
        const customerReviewButton = `<a href="/review/create/customer/${id}" class="button button-green">Review Customer</a>`;

        let reviewContent = "";


        if (type === "customer") {
            if (status === "COMPLETED" && !carReviewed) {
                reviewContent += carReviewButton;
            }

            if (status === "COMPLETED" && !providerReviewed) {
                reviewContent += providerReviewButton;
            }
        } else {
            if (status === "COMPLETED" && !customerReviewed) {
                reviewContent += customerReviewButton;
            }
        }

        divCard.innerHTML += `
                <div class="order__card--right">
                    <a href="/order/display/${id}" class="button button-cyan">View Order</a>
                    ${reviewContent}        
                </div>`;

        orders.appendChild(divCard);
    });
}

async function fetchCurrentOrders() {
    allOrders.classList.remove("active");
    currentOrders.classList.add("active");

    spinner = setUpFetch();

    const response = await fetch(`/${userType}/api/current-orders`);
    const { data, type } = await response.json();

    afterFetch(spinner, data, type);
}

async function fetchAllOrders() {
    currentOrders.classList.remove("active");
    allOrders.classList.add("active");

    spinner = setUpFetch();

    const response = await fetch(`/${userType}/api/all-orders`);
    const {data, type} = await response.json();

    afterFetch(spinner, data, type);



}

const CSS_MAP = {
    "PENDING" : "background-color: #ECE4B7;",
    "ACTIVE" : "background-color: #33CA7F; color: white;",
    "WAITING_FOR_PAYMENT" : "background-color: #7DCFB6;",
    "CANCELLED" : "background-color: #FC9F5B;",
    "REJECTED" : "background-color: #D50000; color: white;",
    "COMPLETED" : "background-color: #00CFC1; color: white;"
}

const allOrders = document.querySelector("#all-orders");
const currentOrders = document.querySelector("#current-orders");
const userType = document.querySelector("#user-type").value;
const orders = document.querySelector(".order");

window.addEventListener("DOMContentLoaded", fetchAllOrders());

allOrders.addEventListener("click", fetchAllOrders);
currentOrders.addEventListener("click", fetchCurrentOrders);
