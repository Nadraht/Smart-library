![Java](https://img.shields.io/badge/Java-17-orange)
![OOP](https://img.shields.io/badge/OOP-Project-blue)
![License](https://img.shields.io/badge/License-Academic-green)

# Smart Library Circulation & Automation System (SLCAS)

A complete **Smart Library Management System** built with **Java and Swing GUI**, demonstrating object-oriented programming, data structures, algorithms, file handling, and modular system design.

This system allows administrators to manage library resources, users, borrowing operations, and reports through a graphical interface.


# Project Overview

The **Smart Library Circulation & Automation System (SLCAS)** simulates the core functionality of a modern library management system. It supports managing books, journals, and magazines, tracking borrow history, generating reports, and maintaining persistent storage.

The system demonstrates key programming concepts including:

* Object-Oriented Programming (OOP)
* Data Structures
* Searching and Sorting Algorithms
* File Persistence
* GUI Development using Java Swing
* System Modularity



# Features

## Library Item Management

* Add books, journals, and magazines
* Remove items from the library
* View all available items

## Borrowing System

* Borrow items
* Return items
* Track borrowing history
* Automatic availability updates

## User Management

* Register library users
* Track user borrowing records

## Search & Sorting

* Search items by:

  * Title
  * Author
  * Year
* Sorting algorithms implemented:

  * Selection Sort
  * Merge Sort
* Binary search for optimized searching on sorted data
* Linear search for optimized keyword searching


## Reservation System

* Queue-based reservation system for unavailable items

## Automated Notifications

* Overdue reminder system using a timer

## Reports

Generate useful reports such as:

* Most borrowed items
* Overdue users
* Library category distribution

## Persistent Storage

The system saves and loads data automatically using text files.

Files used:

* `items.txt`
* `users.txt`
* `borrow_records.txt`



# Technologies Used

| Technology      | Purpose                   |
| --------------- | ------------------------- |
| Java (JDK 17)   | Core programming language |
| Java Swing      | Graphical User Interface  |
| File I/O        | Data persistence          |
| Data Structures | Lists, Queues, Maps       |
| GitHub          | Version control           |
| Draw.io         | UML Diagram               |

Development Tools:

* VS Code
* GitHub Desktop
* Java Extension Pack



# Installation & Setup

Follow these steps to run the project locally.

## 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/smart-library.git
```

or download the ZIP from GitHub and extract it.



## 2. Navigate to the Project Folder

```
smart-library/
```



## 3. Ensure Java is Installed

Install **JDK 17 or newer**.

Check installation:

```bash
java -version
```



## 4. Open the Project

You can open the project using:

* VS Code
* IntelliJ IDEA
* Eclipse

Recommended:
Open the **smart-library folder** as the project root.


## 5. Run the Application

Navigate to:

```
src/gui/MainWindow.java
```

Run the **MainWindow** class.

This launches the Smart Library GUI.


# Project Structure

smart-library
│
├── data
│   ├── items.txt
│   ├── users.txt
│   └── borrow_records.txt
│
├── screenshots
│
├── src
│   ├── controller
│   │   ├── BorrowController.java
│   │   └── SearchEngine.java
│   │
│   ├── gui
│   │   ├── MainWindow.java
│   │   ├── AdminPanel.java
│   │   ├── BorrowPanel.java
│   │   ├── SearchSortPanel.java
│   │   └── ViewPanel.java
│   │
│   ├── model
│   │   ├── LibraryItem.java
│   │   ├── Book.java
│   │   ├── Magazine.java
│   │   ├── Journal.java
│   │   ├── BorrowRecord.java
│   │   ├── UserAccount.java
│   │   ├── Reservation.java
│   │   └── LibraryDatabase.java
│   │
│   └── utils
│       ├── FileHandler.java
│       ├── ReportGenerator.java
│       ├── IdGenerator.java
│       └── OverdueReminder.java
|
└── uml diagram
│
└── README.md

# UML Diagram
![UML Diagram](/uml%20diagram/Smart%20Library.drawio.png)

# Screenshots

![Add Item](/screenshots/Admin%20add%20item.PNG)
![Borrow/Return Item](/screenshots/Borrow%20and%20return.PNG)
![Delete Item](/screenshots/Delete%20Item.PNG)
![Item History](/screenshots/Item%20history.PNG)
![View Items](/screenshots/Items.PNG)
![Register User](/screenshots/Register%20user%20admin.PNG)
![Generate Report](/screenshots/Report%20Admin%20Panel.PNG)
![Search by Keywords](/screenshots/search%20by%20keywords.PNG)
![Search & Sort](/screenshots/Searchsort.PNG)

# Future Improvements

Possible improvements include:

* Database integration (MySQL/PostgreSQL)
* User authentication system
* Advanced search filters
* Web-based interface
* Role-based permissions (Admin / Librarian)


# Contributors

* Nadraht Mohammed
* Demilade Ogundulu
* Enyo-ojo Maji Ishaka
* Fifehanmi Shittu
* Emmanuel Atatigho
* Gabriel Igberaese
* Joshua Adewunmi
* Kehinde Omotosho
* Oluchukwu Daniel
* Segun M Adegbite
* Samuel Omokanju
* Victor Emmanuel
* Zajemeh Guma


# License

This project was developed for academic purposes as part of **COS 202 – Object-Oriented Programming**.
