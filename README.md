CS321 Data Structures: lab4
Bioinformatics
# GeneBank

Authors: Samantha Maxey, Matt Merris, Bridgette Milgie, Aubrey Spannagel, Jonathan Tipton
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Files:
Btree.java: An implementation of a BTree for GeneBanks.
inner class (BTreeNode): A Node class used by the BTree.

TreeObject.java: Object class used by BTree.java.

BTreeCacheNode.java: Cache for BTreeNode.
inner class (BTreeCache): Cache for BTree.

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


Timing Results:



BTree Design:

The program uses a few different classes to accomplish the end goal. These classes are
BTree, BTreeCacheNode, GBFileConvert, GeneBankCreateBTree, GeneBankSearch, and
TreeObject. The BTree will write meta-data, containing the size, degree, and offset
of the root note to the disk. The meta-data for each node, containing the isLeaf variable
and the number of keys, is then written. Each node contains a linked list of child pointers
and TreeObjects, its offset and the offset of its parent. All of this information is then
used to move nodes when manipulating the BTree.
