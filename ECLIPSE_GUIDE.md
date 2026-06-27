# Eclipse IDE Import & Running Guide

Follow this guide to run the **Branded Shoe Store AI Agent** project inside your Eclipse IDE from scratch.

---

## Prerequisites
1. **Eclipse IDE**: Make sure you have Eclipse installed (Eclipse IDE for Java Developers).
2. **Java 25+**: The project is configured for the latest LTS Java runtime, Java 25.

---

## Step 1: Open Eclipse and Select Workspace
1. Launch **Eclipse IDE**.
2. Select any folder of your choice as the workspace (e.g., `C:\eclipse-workspace`) and click **Launch**.

---

## Step 2: Import the Project
Eclipse has built-in Maven support, meaning it will automatically download all dependencies (Spring Boot, Security, JPA, JWT, H2, etc.) for you.

1. Click on **File** in the top menu bar, and select **Import...**
2. In the wizard search box, type **"Maven"**.
3. Select **Existing Maven Projects** and click **Next**.
4. Next to the **Root Directory** field, click **Browse...**
5. Navigate to your workspace directory `D:\Intern_Projects\AGENTIC_AI_PROJECT_MANDELBULB` and select the **`backend`** folder.
6. Make sure the checkbox next to `/pom.xml` is selected.
7. Click **Finish**.

> [!NOTE]  
> After clicking Finish, look at the bottom-right corner of Eclipse. You will see a progress bar saying **"Importing Maven Projects"** or **"Building workspace"**. Eclipse is downloading Spring Boot jars. Please wait until this progress bar reaches 100% and disappears.

---

## Step 3: Run the Spring Boot App
1. In the **Project Explorer** panel on the left, expand the imported `online-store-agent` project.
2. Expand the folders: `src/main/java` -> `com.storeagent`.
3. Locate the file **`StoreAgentApplication.java`**.
4. **Right-click** on `StoreAgentApplication.java` and hover over **Run As**, then select **Java Application** (or **Spring Boot App** if you have Spring Tools installed).
5. The **Console** tab at the bottom will open, and you will see the Spring Boot banner print, followed by logs showing:
   - Schema creation in H2 database.
   - Default user seeding (`admin` and `customer` accounts).
   - Server starting up on port `8080`: `Tomcat started on port 8080 (http)`.

---

## Step 4: Open the Frontend UI
1. Locate the **`frontend`** directory in your explorer: `D:\Intern_Projects\AGENTIC_AI_PROJECT_MANDELBULB\frontend\`.
2. Double-click the **`index.html`** file. This will open the user interface in your default web browser (Chrome, Edge, Firefox, etc.).
3. You are now ready to register new accounts, login, search shoes, view inventory, modify items as an admin, and chat with the AI Agent!

---

## Troubleshooting
* **Red Error Markers on Import**: If you see red exclamation marks on the project, right-click the project name in Eclipse, select **Maven > Update Project...**, check the box for **Force Update of Snapshots/Releases**, and click **OK**.
* **Java Version Conflict**: If Eclipse throws compiler warnings about Java versions, right-click the project -> **Properties** -> **Java Compiler** -> Ensure the compiler compliance level matches your installed JDK.
