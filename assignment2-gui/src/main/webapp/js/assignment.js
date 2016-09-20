var markers = [];

function getStudentData() {
	// This must be implemented by you. The json variable should be fetched
	// from the server, not initiated with a static value as below.
	// You must first download the student json data from the server
	$.ajax({
		url: "http://localhost:8080/assignment2-gui/api/student",
		type: "GET",
		success: function(result){
	       console.log(result);
	       // then call populateStudentTable(json);
	       populateStudentTable(result);
	   	   // and then populateStudentLocationForm(json);
	       populateStudentLocationForm(result);
	    },
	    error: function(er){
	    	console.log(er);
	    }
	});
	

}

function populateStudentTable(json) {
	// for each student make a row in the student location table
	// and show the name, all courses and location.
	// if there is no location print "No location" in the <td> instead
	// tip: see populateStudentLocationForm(json) og google how to insert html
	// from js with jquery.
	// Also search how to make rows and columns in a table with html
	
	//delete previous data
	$('.my_table').remove();
	
	//delete previous markers
	for (var i = 0; i < markers.length; i++) {
	    markers[i].setMap(null);
	}
	markers = [];

	// the table can you see in index.jsp with id="studentTable"
	var formString = '';
	for (var s = 0; s < json.length; s++) {
		var student = json[s];
		student = explodeJSON(student);
		formString += '<tr class="my_table"><td>' + student.name + '</td>';
		
		var stringCourse = '';
		for (var c = 0; c < student.courses.length; c++){
			stringCourse += student.courses[c].courseCode + " ";
		}
		formString += '<td>' + stringCourse +'</td>';
		
		if(student.latitude!=null && student.longitude != null){
			formString += '<td>' + student.latitude + ',' + student.longitude + '</td></tr>';
			
			//map marker
			add_marker(student.latitude, student.longitude, student.name);
		}
		else formString += '<td> No Location </td></tr>';	
	}
	
	$('#studentTable').append(formString);		
}

function populateStudentLocationForm(json) {
	var formString = '<tr><td><select id="selectedStudent" name="students">';
	for (var s = 0; s < json.length; s++) {
		var student = json[s];
		student = explodeJSON(student);
		formString += '<option value="' + student.id + '">' + student.name
				+ '</option>';
	}
	formString += '</select></td></tr>';
	
	$('#studentLocationTable').append(formString);	
}

$('#locationbtn').on('click', function(e) {
	e.preventDefault();
	get_location();
});

// This function gets called when you press the Set Location button
function get_location() {
	navigator.geolocation.getCurrentPosition(
			function(position) {
				if(position.coords.latitude != null && position.coords.longitude != null){
					location_found(position);
				} else {
					alert("browser doesn't handle geolocation");
				}		
			},
			function(er){
				console.log(er);
			}
	);
}

// Call this function when you've succesfully obtained the location.
function location_found(position) {
	// Extract latitude and longitude and save on the server using an AJAX call.
	// When you've updated the location, call populateStudentTable(json); again
	// to put the new location next to the student on the page. .
	//get selected item
	var comboBox = document.getElementById("selectedStudent");
	var user = comboBox.options[comboBox.selectedIndex].value;
	//ajax
	$.ajax({
		url: "http://localhost:8080/assignment2-gui/api/student/"+user+"/location",
		type: "GET",
		data: {
			latitude: position.coords.latitude,
			longitude: position.coords.longitude
		},
		success: function(result){
	       // then call populateStudentTable(json);
	       populateStudentTable(result);
	    },
	    error: function(er){
	    	console.log(er);
	    }
	});
	
	

}

var objectStorage = new Object();

function explodeJSON(object) {
	if (object instanceof Object == true) {
		objectStorage[object['@id']] = object;
		console.log('Object is object');
	} else {
		console.log('Object is not object');
		object = objectStorage[object];
		console.log(object);
	}
	console.log(object);
	return object;
}

var map;
function initialize_map() {
       var mapOptions = {
               zoom : 10,
               mapTypeId : google.maps.MapTypeId.ROADMAP
       };
       map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
       // Try HTML5 geolocation
       if (navigator.geolocation) {
               navigator.geolocation.getCurrentPosition(function(position) {
                       var pos = new google.maps.LatLng(position.coords.latitude,
                                       position.coords.longitude);
                       map.setCenter(pos);
               }, function() {
                       handleNoGeolocation(true);
               });
       } else {
               // Browser doesn't support Geolocation
               // Should really tell the user…
    	   	   alert("Browser doesn't support geolocation");
       }
}

function add_marker(latitude, longitude, name){
	var myLatlng = new google.maps.LatLng(latitude, longitude);                        
	var marker = new google.maps.Marker({
	   position: myLatlng,
	   map: map,
	   title: name
	});
	
	markers.push(marker);
}
