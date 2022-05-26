async function handleResponse(response) {
    if (response.status === 200) {

        jsonResponse = await response.json();
        const SUCCESS_TEXT = "Status successfully updated."

        const statusText = document.querySelector("#status");
        const statusDisplayed = jsonResponse.orderStatus
        statusText.innerText = statusDisplayed.replaceAll("_", " ");
        statusText.style = CSS_MAP[statusDisplayed];

        const successMessageDiv = document.createElement("div");
        const successMessage = document.createElement("p");
        successMessage.innerText = SUCCESS_TEXT;
        
        const toast = document.querySelector(".toast");
        if (toast != null) {
            const toastChild = toast.children[0];

            toastChild.innerText = SUCCESS_TEXT;
            toast.classList.remove("toast-error");
            toast.classList.add("toast-success");
            return;
        }


        const carHeader = document.querySelector(".car-header");
        successMessageDiv.classList.add("toast");
        const cross = document.createElement("div");
        cross.innerHTML = "<i class=\"fa-solid fa-xmark close-error\"></i>";
        cross.addEventListener("click", () => {
            successMessageDiv.remove();
        });
        successMessageDiv.classList.add("toast-success");
        
        successMessageDiv.appendChild(successMessage);
        successMessageDiv.appendChild(cross);
        carHeader.appendChild(successMessageDiv);

        return true;


    } else {

        const errorText = await response.text();

        const toast = document.querySelector(".toast");
        if (toast != null) {
            const toastChild = toast.children[0];
            toastChild.innerText = errorText;
            return;
        }

        const errorMessageDiv = document.createElement("div");
        const errorMessage = document.createElement("p");
        errorMessage.innerText = errorText;
        
        

        const main = document.querySelector(".container");
        errorMessageDiv.classList.add("toast");
        errorMessageDiv.classList.add("toast-error");

        const cross = document.createElement("div");
        cross.innerHTML = "<i class=\"fa-solid fa-xmark close-error\"></i>";
        cross.addEventListener("click", () => {
            errorMessageDiv.remove();
        });

        errorMessageDiv.appendChild(errorMessage);
        errorMessageDiv.appendChild(cross);
        main.appendChild(errorMessageDiv);

        return false;
    }
}

const API_ROUTE = "http://localhost:8090";

async function createRejection (bookingDetails, main) {


    const detailValue = bookingDetails.value;
    const orderId = main.dataset.orderId;

    const json = {
        bookingMessage: detailValue
    }

    const response = await fetch(`${API_ROUTE}/order/reject/${orderId}`, {
        method: "POST",
        headers: {
            "Content-Type" : "application/json"
        },
        body: JSON.stringify(json)
    });

    return await handleResponse(response);  
}

async function createConfirmation(main) {
    const orderId = main.dataset.orderId;

    const response = await fetch(`${API_ROUTE}/order/confirm/${orderId}`, {
        method: "POST",
        headers: {
            "Content-Type" : "application/json"
        },
        body: JSON.stringify({})
    });

    return await handleResponse(response);
}

async function activateOrder(main) {
    const orderId = main.dataset.orderId;

    const response = await fetch(`${API_ROUTE}/order/pay/${orderId}`, {
        method: "POST",
        headers: {
            "Content-Type" : "application/json"
        },
        body: JSON.stringify({})
    });

    return await handleResponse(response);
}

async function completeOrder(main) {
    const orderId = main.dataset.orderId;

    const response = await fetch(`/order/complete/${orderId}`, {
        method: "POST",
        headers: {
            "Content-Type" : "application/json"
        },
        body: JSON.stringify({})
    });

    return await handleResponse(response);
}

const CSS_MAP = {
    "PENDING" : "background-color: #ECE4B7;",
    "ACTIVE" : "background-color: #33CA7F; color: white;",
    "WAITING_FOR_PAYMENT" : "background-color: #7DCFB6;",
    "CANCELLED" : "background-color: #FC9F5B;",
    "REJECTED" : "background-color: #D50000; color: white;",
    "COMPLETED" : "background-color: #00CFC1; color: white;"
}

const bookingDetails = document.querySelector("#booking-msg");
const confirmBtn = document.querySelector("#button-confirm");
const rejectBtn = document.querySelector("#button-reject");
const payBtn = document.querySelector("#button-pay");
const completeBtn = document.querySelector("#button-complete");
const main = document.querySelector(".container");

if (rejectBtn != null) {
    rejectBtn.addEventListener("click", async (e) => {
        e.preventDefault();
        rejectBtn.innerHTML = "<div class=\"lds-dual-ring\"></div>";
        const isValid = await createRejection(bookingDetails, main);

        if (isValid) {
            e.target.parentElement.parentElement.remove();
        }
        rejectBtn.innerHTML = "Reject";
    });
}


if (confirmBtn != null) {
    confirmBtn.addEventListener("click", async (e) => {
        e.preventDefault();
        confirmBtn.innerHTML = "<div class=\"lds-dual-ring\"></div>";
        const isValid = await createConfirmation(main);
        
        if (isValid) {
            e.target.parentElement.parentElement.remove();
        }
        confirmBtn.innerHTML = "Confirm";
    });
}    

if (payBtn != null) {
    payBtn.addEventListener("click", async (e) => {
        e.preventDefault();
        payBtn.innerHTML = "<div class=\"lds-dual-ring\"></div>";
        const isValid = await activateOrder(main);

        if (isValid) {
            e.target.remove();
        }
        payBtn.innerHTML = "Accept Payment";
    })   
}

if (completeBtn != null) {
    completeBtn.addEventListener("click", async (e) => {
        e.preventDefault();
        completeBtn.innerHTML = "<div class=\"lds-dual-ring\"></div>";
        const isValid = await completeOrder(main);
        
        if (isValid) {
            e.target.remove();
        }
        completeBtn.innerHTML = "Complete";
    })
}