# Queue Simulator

**Queue Simulator** is a Java-based application designed to model, simulate, and analyze queue systems. This simulator was developed as a project in the basics of object-oriented programming course, aiming to provide insights into real-world phenomena through simulation.

---

## Features

- **Three-Phase Simulation**: Simulate real-world queue scenarios with detailed components.
- **Data Analysis**: Generate outputs such as average waiting times, operational costs, and revenue insights.
- **Customizable Parameters**: Define workers, tables, and pricing models for tailored scenarios.
- **Visualization**: View real-time simulation progress and performance metrics.
- **Extensible Design**: Easily adaptable for additional features and complex systems.

---

## Technical Specifications

- **Programming Language**: Java (JDK 8 or higher)
- **Libraries**: JavaFX for GUI, Hibernate for database handling
- **Build Tool**: Maven 3.6+
- **Database**: Local SQL database for simulation data storage
- **Testing Framework**: JUnit for unit testing

---

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)**: Version 8 or higher
- **Maven**: Version 3.6+
- **IDE**: IntelliJ IDEA or any Java-compatible IDE

### Setup

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/Jonnerop/Queue-Simulator/tree/main
   cd Queue-Simulator
   ```

2. **Build the Project**: Use Maven to clean and install dependencies:

   ```bash
   mvn clean install
   ```

3. **Run the Application**: Use your IDE to execute `Main.java`, or run via Maven:

   ```bash
   mvn exec:java -Dexec.mainClass="Main"
   ```

4. **Testing**: Run tests with Maven:

   ```bash
   mvn test
   ```

---

## Usage

1. **Launch the Application**: Open the project in your IDE and run `Main.java`.
2. **Configure Simulation**: Adjust parameters such as worker counts, table availability, and menu prices.
3. **Start Simulation**: Observe queue behavior and analyze key metrics such as wait times and resource utilization.
4. **Export Results**: Save outputs for further review and decision-making.

---

## Troubleshooting

- **Build Issues**:
  - Ensure Maven and JDK are correctly installed.
- **Simulation Errors**:
  - Verify input parameters and ensure proper setup.
- **Database Connectivity**:
  - Run the SQL setup script before the first simulation.

---

## Documentation (in Finnish)

- [Project Document](./Project_Document.pdf)
- [User Manual](./User_Manual.pdf)

---

## Contributors

- **Ade Aiho**
- **Heta Hartzell**
- **Mika Laakkonen**
- **Jonne Roponen**
