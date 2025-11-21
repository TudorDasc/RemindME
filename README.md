A caregiver app that allows the user to receive updates, modify, or add routines to the elderly’s devices. The app was specifically made for a prototype device made to aid the elderly in everyday tasks. The prototype is shown below. 

<img width="359" height="200" alt="image" src="https://github.com/user-attachments/assets/50c0f6b6-e20c-4644-8c94-d5b8fb0c2af1" />

The app was built using Java as a programming language and Android Studio as the main IDE, since it offers a large variety of features that can be implemented. Moreover, it also allows the developer to create a direct connection with the database and with the authentication system required for the login/register features.

1. Login Screens:

<img width="699" height="581" alt="image" src="https://github.com/user-attachments/assets/2c03b184-0e49-4424-86d5-f0c2741f45f4" />


2. App Features:
 
<img width="811" height="425" alt="image" src="https://github.com/user-attachments/assets/cfedc501-a526-4d16-ae15-bb787a0e854d" />


Additionally, the app required a method to store the data in a way that could be easily scaled as the product is used more and more. To this extent, I considered multiple ways of implementing the data storage and ended up choosing the realtime database provided by Firebase due to its flexibility to change and to be updated rapidly.

An example of the routine data structure created is illustrated below:

<img width="539" height="482" alt="image" src="https://github.com/user-attachments/assets/369b553f-0312-4bd5-9887-b5b7244f85f1" />

Example of the "elderly" users and how I attached routines to their accounts. The elderly users are uniquely identified by their ID:

<img width="501" height="398" alt="image" src="https://github.com/user-attachments/assets/1370c2d9-bc6c-4a3d-ba18-053ba5e34e17" />

Lastly, an example of a caregiver who can add routines to an "elderly" user. The caregiver users are uniquely identified by their ID, and are one way associated to the ID of the elderly users:

<img width="661" height="275" alt="image" src="https://github.com/user-attachments/assets/9377e499-00de-42fa-91cd-b315dfdab083" />
