# stepper
Stepper - Customizable Workflow/Pipeline System

Stepper is a customizable workflow/pipeline system that empowers users to assemble and execute different scenarios, known as flows, using reusable components called steps. This robust system facilitates the execution of flows, generates desired results, and provides valuable insights through information and statistics collection.

Key Features:

Workflow assembly: Define and configure the steps that compose your workflow.
Execution options: Run flows using various UI clients, including a simple CLI and a Desktop client.
User and permission management: Efficiently manage users and permissions within the system.
Concurrent client support: Serve multiple clients concurrently, ensuring smooth execution of flows.
Progress tracking: Collect comprehensive information and statistics on flow progress.
Technologies Used:

Java programming language
JSON serialization for data handling
Multithreading for improved performance
Upcoming Feature:

Client-server architecture: Enabling distributed flow execution across multiple systems.
Usage:
Stepper is designed to provide flexibility and adaptability, allowing you to create workflows and pipelines tailored to your specific needs. Follow these steps to get started:

Define the steps required for your workflow.
Assemble these steps into a flow that represents your desired workflow structure.
Execute the flow using the provided UI clients or programmatically.
Feel free to customize and enhance your workflows as per your unique requirements.

The Admin stepper application start to run
![img_11.png](img_11.png)

Here the Admin need to load xml file that contain details about the flows and the steps with more details (aliasing and initial inputs etc.)
![img.png](img.png)

Screen that see the current user in the system (now it's empty)
![img_1.png](img_1.png)

See the main screen in the Admin app the show the available flows
![img_2.png](img_2.png)

After the Admin load Xml with flows
we can see the flow that the Admin loaded in the Admin screen
![img_3.png](img_3.png)

Admin statistics screen 
![img_4.png](img_4.png)

Admin history screen
![img_5.png](img_5.png)

Client screen + press on the first flow to run
click on the execute button and move to the execute screen  
(we give the option to see more details about the flow)
![img_6.png](img_6.png)

take the free input from the user (this is the input that the user must supply)
![img_7.png](img_7.png)

after execute the flow we get a pop-up of the end of the flow with details like logs and output of the flow and
details about the execution status
![img_8.png](img_8.png)

go to the history screen see details about the flow with the option to run in again without to supply the free inputs again
![img_9.png](img_9.png)

see statistics from the admin screen
![img_10.png](img_10.png)

