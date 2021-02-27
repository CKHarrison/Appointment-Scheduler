# Appointment Scheduler

This is an appointment scheduling application created with Java 11 and Java FX.
A user is able to log in using the username/password combination of test,test. The login screen detects the user's default
location, and displays all login text in either English or French. The user's timezone ID is displayed at the top of the 
login form. Once logged in the user is able to create customers, schedule appointments tied to those customers and generate 
reports. Both customers and appointments have full CRUD capability. When deleting a customer, all appointments associated
with that customer must also be deleted, as enforced by database referential integrity rules. Scheduling appointment times
are done in the user's local time, and then converted to Eastern time to compare with business hours, which fall between
8AM and 10PM. Appointments with the same customer cannot have overlapping times. 
A report view is also available showing the all the different types of appointments and their monthly count. A contact's
schedule can also be generated, along with a user's schedule.

This project is connected to a MySQL backend and is interfaced through the MySQL JDBC Connector Version 8.0.22


## To run the program
Click the run button and log in with the username: test password: test
The Javadoc information can be found in the Javadoc directory in the root folder. Open it and click on index.html to see
the documentation.

Please let me know if you have any questions or comments on this project. I had a great time working on it.