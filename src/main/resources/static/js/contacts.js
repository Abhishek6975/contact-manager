
console.log("This is abhisek")
const baseUrl = "http://localhost:8081";
document.addEventListener('DOMContentLoaded', () => {
	// Get the modal and button elements
	const modal = document.getElementById('view_contact_modal');
	const closeModalBtn = document.getElementById('close-modal');
	const closeModalBtnSecondary = document.getElementById('close-modal-btn');


	// Function to show the modal
	const openContactModal = () => {
		modal.classList.remove('hidden'); // Remove 'hidden' class to show modal
	};

	const hideModal = () => {
		modal.classList.add('hidden'); // Add 'hidden' class to hide modal
	};


	window.loadContactModel = async (id) => {
		console.log(id);
		/*
		// Here you can add your logic to fetch and display the contact details
		fetch(`http://localhost:8081/api/contact/${id}`).then(async (response) => {
			let data = await response.json();
			console.log(data);
			openContactModal(); // Open modal after loading the contact data
			return data;
		}).catch((error) => {
			console.log(error);
		});
		*/

		try {
			const data = await (
				await fetch(`${baseUrl}/api/contact/${id}`)
			).json();

			console.log(data);
			document.querySelector('#contact_name').innerHTML = data.name;
			document.querySelector('#contact_email').innerHTML = data.email;
			document.querySelector("#contact_image").src = data.picture;
			document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
			document.querySelector("#contact_address").innerHTML = data.address;
			document.querySelector("#contact_about").innerHTML = data.description;

			const contactFavorite = document.querySelector("#contact_favorite");
			if (data.favorite) {
				contactFavorite.innerHTML =
					"<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";
			} else {
				contactFavorite.innerHTML = "Not Favorite Contact";
			}

			document.querySelector("#contact_website").href = data.websiteLink;
			document.querySelector("#contact_website").innerHTML = data.websiteLink;
			document.querySelector("#contact_linkedIn").href = data.linkedInLink;
			document.querySelector("#contact_linkedIn").innerHTML = data.linkedInLink;

			openContactModal();
		} catch (error) {

			console.log("Error :", error);
		}
	};
	closeModalBtn.addEventListener('click', hideModal);
	closeModalBtnSecondary.addEventListener('click', hideModal);

	window.openContactModal = openContactModal;
	window.loadContactModel = loadContactdata;

});


async function deleteContact(id) {
	console.log("Delete Contact",id);
  Swal.fire({
    title: "Do you want to delete the contact?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Delete",
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      const url = `${baseUrl}/user/contacts/delete/` + id;
      window.location.replace(url);
    }
  });
}



function exportData() {
    // Initialize Table2Excel and export the table
    var table2excel = new Table2Excel({
        defaultFileName: "contacts", // Set the file name without extension here
        defaultFileExtension: ".xlsx" // Ensure the file extension is .xlsx
    });
    
    // Export the table
    table2excel.export(document.getElementById("contact_table1"));
}





