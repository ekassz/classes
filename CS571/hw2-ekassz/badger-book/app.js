//API
//to be able to use data anywhere + made it more easier
let globalData = [];
fetch('https://cs571.org/api/s24/hw2/students' ,{
	headers: {
		"X-CS571-ID": CS571.getBadgerId()

	}
})
.then(res => {
	if(res.status === 200 || res.status === 304) {
		return res.json()
	} else {
		throw new Error();
	}
	//res.json()
})
.then(data => {
	console.log(data);
	document.getElementById("num-results").innerText = `${data.length}`
	globalData = data;
	badgerBook(data);
})

/**
 * step 3, 4, 6
 * making data pop up, doing grid for sizes of device and making the interests
 * of students clickable to transfer into the search input 
 * 
 * @param {*} students 
 */
function badgerBook(students){
	const studentsContainer = document.getElementById("students");
	//studentsContainer.innerHTML = ''; cant do that
	//students data
	//grid
	studentsContainer.className = "row";
	
	students.forEach(student => {
		
		//have to make container
		const studentDiv = document.createElement("div");
		//grid
		studentDiv.className = 'col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3';
	
		const nameParagraph = document.createElement("h3");
		nameParagraph.innerText = `${student.name.first} ${student.name.last}`;
		studentDiv.appendChild(nameParagraph);

		const majorParagraph = document.createElement("p");
		majorParagraph.innerText = `${student.major}`;
		majorParagraph.style.fontWeight = 'bold';
		studentDiv.appendChild(majorParagraph); 

		const creditsWisco = document.createElement("p");
		creditsWisco.innerText = student.fromWisconsin ? `${student.name.first} is taking ${student.numCredits} credits and is from Wisconsin` : 
		`${student.name.first} is taking ${student.numCredits} credits and is NOT from Wisconsin`; 
		studentDiv.appendChild(creditsWisco);
		

		const interestsS = document.createElement("p");
		interestsS.innerText = `They have ${student.interests.length} interests including...`;
		studentDiv.appendChild(interestsS);

		//interests
		const interestsUL = document.createElement("ul");
		student.interests.forEach(interest => {
			const interestsLI = document.createElement("li");
			//step 6 make interest clickable and generate students with the same one
			const interestMaker = document.createElement("a");
			interestMaker.href = '#';
			interestMaker.innerText = interest;
			//to know its clickable
			interestMaker.style.cursor = "pointer";
			interestMaker.addEventListener("click", (e)=> {
				e.preventDefault();
				//search interest input -> clicked inter
				document.getElementById('search-interest').value = interest;
				document.getElementById('search-name').value = '';
				document.getElementById('search-major').value = '';
				//search
				handleSearch();
			});
			
			interestsLI.appendChild(interestMaker);
			interestsUL.appendChild(interestsLI);
		});
		studentDiv.appendChild(interestsUL);

		studentsContainer.appendChild(studentDiv);



	});
}

/**
 * step 5
 * Have to make the search input boxes work with substrings, case-insensitive, and have each one work seperately
 * also combine top with the updated count of how many output student bios there are.
 * @param {} e 
 */
function handleSearch(e) {
	e.preventDefault(); // You can ignore this; prevents the default form submission!

	// TODO Implement the search
	//get any input and make it lowercase, get all names and make it lowercase
	const searchName = document.getElementById('search-name').value.trim().toLowerCase();
	const searchMajor = document.getElementById('search-major').value.trim().toLowerCase();
	const searchInterest = document.getElementById('search-interest').value.trim().toLowerCase();

	//innerHTML - to clear display
	const studentsContainer = document.getElementById('students');
	studentsContainer.innerHTML = '';

	//compare data and filter for search
	const compareData = globalData.filter(student => {
		//to be able to compare later and see if match
		const fullName = `${student.name.first} ${student.name.last}`.toLowerCase(); 
		const major = student.major.toLowerCase();
		const interestsInfo = student.interests.some(interest => interest.toLowerCase().includes(searchInterest));

		//substring comparisons to consider the same
		const nameMatch = !searchName || fullName.includes(searchName);
		const majorMatch = !searchMajor || major.includes(searchMajor);
		const interestMatch = !searchInterest || interestsInfo;

		return nameMatch && majorMatch && interestMatch;
	});
	//for num-results, have to update website with the num found
	document.getElementById("num-results").innerText = `${compareData.length}`;
	badgerBook(compareData);
}

document.getElementById("search-btn").addEventListener("click", handleSearch);