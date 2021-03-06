CS321 Data Structures: Program 4
Bioinformatics
# GeneBank

Authors: Samantha Maxey, Matt Merris, Bridgette Milgie, Aubrey Spannagel, Jonathan Tipton
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Files:
Btree.java: An implementation of a BTree for GeneBanks.
Inner Class (BTreeNode): A Node class used by the BTree.

TreeObject.java: Object class used by BTree.java.

BTreeCacheNode.java: Cache for BTreeNode.
Inner Class (BTreeCache): Cache for BTree.

GBFileConvert.java: Class to represent Gene Sequences (converts from String to binary long).

GeneBankCreateBTree.java: Creates the BTree for the GeneBank with all the Gene Sequences.

GeneBankSearch.java: Searches the BTree and prints the frequency of each Gene Sequence found.

README: This document.


Compile:
$ javac *.java

Run:

GeneBankCreateBTree.java:
$ java GeneBankCreateBTree <cache> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]


**GeneBankCreateBTree must be run first, as a BTree file is a require argument for GeneBankSearch**    
GeneBankSearch.java:
$ java GeneBankSearch <cache> <btree file> <query file> [<cache size>] [<debug level>]


BTree Design:

The program uses a few different classes to accomplish the end goal. These classes are
BTree, BTreeCacheNode, GBFileConvert, GeneBankCreateBTree, GeneBankSearch, and
TreeObject. The BTree will write meta-data, containing the size, degree, and offset
of the root note to the disk. The meta-data for each node, containing the isLeaf variable
and the number of keys, is then written. Each node contains a linked list of child pointers
and TreeObjects, its offset and the offset of its parent. All of this information is then
used to move nodes when manipulating the BTree.

Discussion:
Over the course of 3 weeks, we had gotten together through text and in person to put 
together this project. It was divided so that all 5 of us could have something to do.
Bridgette and Aubrey worked on BTree.java and TreeObject.java; Jonathan worked on 
GeneBankSearch.java and GBFileConvert.java; Samantha worked on GeneBankCreateBTree.java;
and Matt worked on debugging the different classes. 
We are of the belief that GeneBankCreateBTree and 
GeneBankSearch are otherwise functioning(without cache capabilities) based on testing performed. 


Extra Credit:
Cache implemented

    Using:
    java GeneBankCreateBTree 0 0 test3.gbk 6 1 
    the time came out to 5 min, 9sec. 
    
    Using:
    java GeneBankCreateBTree 1 0 test3.gbk 6 100 1 
    the time came out to 3 min, 47sec.
    
    Using:
    java GeneBankCreateBTree 1 0 test3.gbk 6 500 1
    the time came out to 3 min, 33sec.
