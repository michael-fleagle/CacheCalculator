//Author: Michael Fleagle
//Student ID: 41394334
//Honor Code: I did not recieve help on this. This is my work and soley my work.

import java.io.IOException;
import java.util.Scanner;

public class CacheCalculator {

    public static void main(String[] args) throws IOException {
        //Create Scanner
        Scanner input = new Scanner(System.in);

        //Main mem
        System.out.println("Main memory representation is A x 2^E");

        //Get value A
        System.out.print("Value A: ");
        int aVal = input.nextInt();

        //Get value E
        System.out.print("Exponent E: ");
        int eVal = input.nextInt();

        //Calculate and display the main memory in bytes
        int mMem = (int) (aVal * (Math.pow(2, eVal)));

        //Print main mem and calculation
        System.out.println("Main Memory: " + aVal + " x 2^" + eVal + " = " + mMem + " bytes");

        //handle if A is 2
        if(aVal == 2){
            eVal += 1;
        }

        //Accept three inputs for cache blocks, bytes, and k-set
        //Cache blocks
        System.out.print("Cache blocks: ");
        int cBlock = input.nextInt();

        //Verify that cache blocks are a base two value
        if(cBlock % 2 != 0){
            System.out.println("Cache Blocks is not in Base 2. Program terminated");
            return;
        }

        //Bytes in cahce blocks
        System.out.print("Bytes in Cache blocks: ");
        int bCBlock = input.nextInt();

        //Verify that bytes in cache blocks are base 2
        if(bCBlock % 2 != 0){
            System.out.println("Bytes in Cache blocks is not in Base 2. Program terminated");
            return;
        }

        //K-set associative values
        System.out.print("k-set associative value: ");
        int kVal = input.nextInt();

        //Verify that k-set associative values are base 2
        if(kVal % 2 != 0){
            System.out.println("k-set associative values is not in Base 2. Program terminated");
            return;
        }

        //Take input Memory address in base 16
        System.out.print("Memory address in Base 16: ");
        String memAddress = input.next();

        //Verify that mem address is base 16
        if(isBase16(memAddress) == false){
            System.out.println("Memory address is not in Base 16. Program terminated");
            return;
        }

        //print address in binary after converting mem to binary
        StringBuilder binMem = new StringBuilder(hexToBinary(memAddress));

        //Verify that mem address is smaller than main mem
        //not implemented fully
        if(eVal < binMem.length()){
            System.out.println("Size of address is larger than main memory size. Program terminated");
            return;
        }

        while (binMem.length() < eVal) {
            binMem.append("0");
        }
        System.out.println("Address in binary: " + binMem);


        //calculate 2^x power for block, bytes, associative val
        int powBlock = twoPower(cBlock);
        int powBICB = twoPower(bCBlock);
        int powKSet = twoPower(kVal);



        //Direct cache mapping
        int dTag;
        String binDTag; //00110010011
        int dLine = powBlock;
        String binDLine; //01010
        int dWord = powBICB;
        String binDWord; //0000


        //calculate dTag
        dTag = (eVal - (dLine + dWord));

        //calculate bin datag
        binDTag = binMem.substring(0, dTag);

        //calculate bin dline
        binDLine = binMem.substring(dTag, dTag+dLine);

        //calculate bin dword
        binDWord = binMem.substring(binMem.length()-dWord, binMem.length());

        //Print direct cache message
        System.out.println("\nDirect Cache mapping of " + memAddress.toUpperCase() + " address");

        //print direct cache tag, line, word
        System.out.println("[TAG] " + dTag + " : [LINE] " + dLine + " : [WORD] " + dWord);
        //print direct cache binary tag, line, word
        System.out.println("[TAG] " + binDTag + " : [LINE] " + binDLine + " : [WORD] " + binDWord);
        //Empty line for spacing
        System.out.println("");



        //Associative cache mapping
        int aTag;
        String binATag; //0011001001101010
        int aWord = powBICB;
        String binAWord; //0000

        //calculate aTag
        aTag = (eVal - (aWord));

        //calculate binATag
        binATag = binMem.substring(0, aTag);

        //calculate binAWord
        binAWord = binMem.substring(aTag, binMem.length());

        //print associative cache message
        System.out.println("Associative Cache mapping of " + memAddress.toUpperCase() + " address");

        //print associative cache tag, line, word
        System.out.println("[TAG] " + aTag + " : [WORD] " + aWord);
        //print associative cache binary tag, line, word
        System.out.println("[TAG] " + binATag + " : [WORD] " + binAWord);
        //Empty line for spacing
        System.out.println("");



        //4-way cache memory
        int saTag;
        String binSATag;
        int saSet = (powBlock-powKSet);
        String binSASet;
        int saWord = powBICB;
        String binSAWord;

        //Calculate saTag
        saTag = (eVal - (saSet + saWord));

        //calculate binSATag
        binSATag = binMem.substring(0, saTag);

        //calculate bin dline
        binSASet = binMem.substring(saTag, saTag+saSet);

        //calculate bin dword
        binSAWord = binMem.substring(binMem.length()-saWord, binMem.length());

        //print set-associative cache message
        System.out.println("4-way Cache mapping of " + memAddress.toUpperCase() + " address");

        //print set-associative cache tag, line, word
        System.out.println("[TAG] " + saTag + " : [Set] " + saSet + " : [WORD] " + saWord);
        //print set-associative cache binary tag, line, word
        System.out.println("[TAG] " + binSATag + " : [Set] " + binSASet + " : [WORD] " + binSAWord);
        //Empty line for spacing
        System.out.println("");

    }




    public static boolean isBase16(String inputVal){

        //create neded variables
        char ch;
        //process base 16 value to remove "0x" from begining
        String[] valString = inputVal.split("x");
        String val = valString[1];
        val = val.toUpperCase();

        //check if value is base 16
        for (int i = 0; i < val.length(); i++) {

            //get character
            ch = val.charAt(i);

            if (! (((int)ch >= '0' && (int)ch < ('0' + 16)) || ((int)ch >= 65 && (int)ch < 70))) {
                 return false;
            }
        }
        return true;
    }




    //method to convert decimal to binary because I cannot come up with a better way
    public static String decToBinary(int dec)
    {

        //create binary string variable
        String bin = "";

        //generate binary
        while (dec != 0) {

            //use remainder method to get binary
            bin = (dec % 2) + bin;

            //update decimal with next iteration/2
            dec = dec/2;
        }

        //ensure that each hex is 4 bits
        while (bin.length() % 4 != 0) {

            //add leading 0s
            bin = "0" + bin;
        }

        //return binary string
        return bin;
    }



    //method converting hex to binary
    public static String hexToBinary(String hex) {

        //declare needed variables
        char ch;
        String bin = "";
        int retBinary;

        //split hex on x and uppercase
        String[] hexArr = hex.split("x");
        hex = hexArr[1];
        hex = hex.toUpperCase();

        //iterate through string to check each character
        for (int i = 0; i < hex.length(); i++) {

            //extract characters
            ch = hex.charAt(i);

            //check viable hex alphabet
            if ((int)ch >= 65 && (int)ch <= 70) {

                // use ASCII code to convert char to val
                retBinary = (int) ch - 55;

            } else {

                retBinary = Integer.parseInt(String.valueOf(ch));
            }

            //convert decimal to binary
            bin += decToBinary(retBinary);
        }

        //return binary
        return bin;

    } //end of hexToBinary



    //method to get 2^x for given value
    public static int twoPower(int val){
        int pow = 0;
        int i = 2;

        while(i <= val){
            i *= 2;
            pow++;
        }
        //return power
        return pow;
    }


} //end of CacheCalculator
