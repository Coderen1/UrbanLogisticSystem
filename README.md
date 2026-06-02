# FastRoute_Kayseri (COMP206)

This project is a console-based **urban logistics and distribution simulation** developed for the COMP206 course.  
It models cargo operations in Kayseri to demonstrate core data structures and graph algorithms in a realistic scenario.

## Project Overview

When the program starts:
- The road network is loaded from `mapData.txt`.
- Initial package data is loaded from `packageData.txt`.
- Delivery, address, and road operations are managed step by step through an interactive menu.

Typical package flow in the system:
1. Receive package (intake buffer)
2. Register in master log
3. Move to delivery queue
4. Load onto truck
5. Deliver (unload from truck)

## Data Structures and Algorithms Used

- **Doubly Linked List**: Temporarily stores incoming packages (`insertAtTail`, `removeFromHead`)
- **Singly Linked List**: Maintains the daily master log (`addRecord`, `displayLog`)
- **Queue (FIFO)**: Holds packages waiting for delivery in order (`enqueue`, `dequeue`)
- **Stack (LIFO)**: Simulates truck loading/unloading behavior (`push`, `pop`)
- **AVL Tree**: Stores neighborhood-customer mappings in balanced form (`insert`, `search`, `displayInOrder`)
- **Graph**:
  - Shortest path: **Dijkstra**
  - Minimum spanning tree: **Prim (MST)**

## Project File Structure

- `Main.java`: Program entry point and menu-based operation control
- `Graph.java`, `Edge.java`: Road network model, edge management, shortest path, and MST operations
- `AVLTree.java`, `AVLNode.java`: AVL tree implementation and rotation logic
- `DoublyLinkedList.java`, `DLLNode.java`: Intake buffer structure for incoming packages
- `SinglyLinkedList.java`, `SLLNode.java`: Daily master log structure
- `PackageQueue.java`, `QueueNode.java`: Delivery queue implementation
- `PackageStack.java`, `StackNode.java`: Truck loading stack implementation
- `Package.java`: Package model
- `mapData.txt`: Road and distance data
- `packageData.txt`: Initial package dataset

## Menu Operations

The program menu provides the following operations:

1. Receive a new delivery  
2. Display waiting deliveries  
3. Register delivery in the master log  
4. Display daily master log  
5. Process deliveries into the queue  
6. Display delivery queue  
7. Load truck from queue  
8. Display loaded truck stack  
9. Deliver/unload package from truck  
10. Create a new address (AVL)  
11. Search address (AVL)  
12. Display all addresses (in-order)  
13. Add a new road connection  
14. Display city road map  
15. Calculate shortest path between two areas  
16. Display minimum spanning tree (MST)  
17. AVL balancing/rotation test
