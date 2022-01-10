Disclaimer: Can't guarantee correct Results

Made using Java 8 (temurin) [select in File: Project Structure]
To use:
Copy a part of the Main Class into its own class in run
This class has to extend Base (see Test1, Test2)

Matrix Operations: [D represents a number] (always use exact format)
//FACTOR: rD*D // rD*!cDvD
//ADD: rD+DrD // rD+rD // rD+rD!cDvD
//SWAP rD<>rD
//SUB: rD-DrD // rD-rD // rD-rD!cDvD
//CONTROL: u // e [for undo and exit. Exit will print the calculation afterwards]

You can concat multiple Operations into one Step as long as they don't conflict



cDvD can be used to get a value in a column

[in Z5]
(0 0 4)
r1*!c3v1 // in this case equivalent to r1*4
(0 0 1)
