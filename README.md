# Elevator Control Real-Time System

A group project for the **Software Development for Real-Time Systems** module.

This project implements a real-time elevator control system using Java. The system simulates elevator movement, request handling, door control, emergency handling, authentication/access control, event processing, and real-time GUI updates.

---

## Project Overview

The elevator control system manages a multi-floor elevator by handling user requests, processing events, controlling the motor and doors, monitoring sensors, and responding to emergency situations.

The system is designed around real-time system concepts such as:

- Event-driven processing  
- Finite state machine behavior  
- Multithreading  
- Synchronization  
- Sensor-based decision making  
- Real-time UI updates  
- Authentication and restricted floor access  

---

## Main Features

- Call elevator from outside  
- Select destination floor from inside the elevator  
- Queue and process elevator requests  
- Move elevator up or down based on destination  
- Detect floor arrival using floor sensor events  
- Open and close elevator doors  
- Handle door obstruction detection  
- Emergency mode handling  
- Access control using authentication token  
- Event logging  
- Real-time GUI interaction  
- Esper-based event handling  

---

## Project Structure

```text
Source Packages
│
├── esper
│   ├── Config.java
│   └── Main.java
│
├── events
│   ├── AccessRequestEvent.java
│   ├── AuthEvent.java
│   ├── CallElevatorEvent.java
│   ├── DoorEvent.java
│   ├── EmergencyEvent.java
│   ├── FloorReachedEvent.java
│   ├── FloorSensorEvent.java
│   └── MotorEvent.java
│
├── model
│   ├── AccessControlService.java
│   ├── Door.java
│   ├── DoorSensor.java
│   ├── ElevatorControlSystem.java
│   ├── ElevatorRequest.java
│   ├── EmergencySensor.java
│   ├── EventLogger.java
│   ├── FloorSensor.java
│   ├── KeyReaderSensor.java
│   ├── Motor.java
│   └── RequestQueue.java
│
└── view
    ├── ElevatorPhase1View.java
    └── ElevatorPhase2View.java
