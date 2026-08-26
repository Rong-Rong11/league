# NBA League Simulator

[English](README.md) | [Français](README.fr.md)

Desktop application that simulates an NBA season through a Java Swing interface.

> This application was developed as a **university team project** during the second year of a Computer Science degree. This repository preserves the project's collective origin and Git history.

![Match simulation, statistics and game finances](docs/images/match-simulation.png)

## Features

- Select and manage an NBA team
- Configure the team's initial financial parameters
- Simulate the regular season and playoffs
- Browse the calendar, game results and league standings
- Follow live game actions and statistics
- Inspect team, league and game finances
- Visualize financial data with charts
- Review rosters and player trades
- Switch to a dark theme for improved visual comfort and different display preferences

## Screenshots

| League setup and financial policies | Financial analysis |
| --- | --- |
| ![League setup and financial policy configuration](docs/images/league-setup.png) | ![Financial analysis with revenue and expense charts](docs/images/financial-analysis.png) |

| Playoff bracket | Dark theme |
| --- | --- |
| ![Playoff bracket and season progression](docs/images/playoffs.png) | ![Dark theme applied to the application interface](docs/images/dark-theme.png) |

The dark theme provides an alternative display intended to improve visual comfort. This is a user-interface option, not a claim that the application has undergone a complete accessibility audit.

## Technology

- Java and Swing
- JFreeChart / JCommon for data visualization
- Log4j 1.2 for application logging
- JUnit 4 for automated tests
- CSV resources for league data

Dependencies are included in [`lib/`](lib/), so no package manager is required.

## Run locally

### Requirements

- JDK 8 or later
- A terminal: macOS/Linux shell or Windows PowerShell

### macOS / Linux

From the repository root, compile the application:

```bash
mkdir -p out
find src -name '*.java' -print0 \
  | xargs -0 javac -encoding UTF-8 -cp 'lib/*' -d out
```

Start it with:

```bash
java -cp 'out:src:lib/*' gui.app.App
```

### Windows PowerShell

From the repository root, create the output directory and compile every Java source file:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$Sources = Get-ChildItem -Path src -Recurse -Filter *.java |
  ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp "lib/*" -d out $Sources
```

Start the application with:

```powershell
java -cp "out;src;lib/*" gui.app.App
```

> The PowerShell commands follow the Windows classpath syntax (`;`) but have not been executed on a Windows machine as part of this repository verification.

The application entry point is [`src/gui/app/App.java`](src/gui/app/App.java).

## Tests

The project contains unit, usage, robustness and performance tests under [`src/test/`](src/test/). The unit-test directory currently contains 24 JUnit test classes and 124 methods annotated with `@Test`.

After compiling the project, one test class can be run on macOS/Linux with:

```bash
java -cp 'out:src:lib/*' \
  org.junit.runner.JUnitCore test.unit.TestFinanceTypeResolver
```

Or on Windows PowerShell with:

```powershell
java -cp "out;src;lib/*" org.junit.runner.JUnitCore test.unit.TestFinanceTypeResolver
```

To run every unit-test class from a macOS/Linux shell:

```bash
TEST_CLASSES=$(find src/test/unit -name 'Test*.java' \
  | sed 's#src/##; s#/#.#g; s#\.java$##' \
  | tr '\n' ' ')

java -cp 'out:src:lib/*' org.junit.runner.JUnitCore $TEST_CLASSES
```

To run every unit-test class from Windows PowerShell:

```powershell
$TestClasses = Get-ChildItem -Path src/test/unit -Recurse -Filter 'Test*.java' |
  ForEach-Object {
    $_.FullName.Substring((Resolve-Path src).Path.Length + 1) `
      -replace '\\', '.' -replace '\.java$', ''
  }

java -cp "out;src;lib/*" org.junit.runner.JUnitCore $TestClasses
```

## Project structure

```text
src/
├── config/       # Simulation and finance configuration
├── data/         # League, team, player, game and financial models
├── gui/          # Swing frames, dashboards, panels and components
├── log/          # Log4j configuration
├── process/      # Builders, services, simulation and orchestration
├── resources/    # CSV league data and interface assets
└── test/         # Automated tests
```

## My contribution

My contribution to this team project focused on:

- developing parts of the graphical user interface with Java Swing;
- contributing to functional design discussions;
- helping plan and prioritize the work needed to satisfy the assignment requirements and deadline.

The project was not developed individually. The fork relationship and Git history are intentionally preserved to credit the complete team.

## Academic context

Developed as a Licence 2 Computer Science software-engineering project.
