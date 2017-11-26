import java.util.ArrayList;

// Block Chain should maintain only limited block nodes to satisfy the functions
// You should not have all the blocks added to the block chain in memory 
// as it would cause a memory overflow.



public class BlockChain {
    public static final int CUT_OFF_AGE = 10;

    /**
     * create an empty block chain with just a genesis block. Assume {@code genesisBlock} is a valid
     * block
     */
    ArrayList<Block> blockChain;
    Block maxHeightBlock;
    TransactionPool transactionPool;
    
    public BlockChain(Block genesisBlock) {
        // IMPLEMENT THIS
    	this.blockChain = new ArrayList<Block>();
    	this.blockChain.add(genesisBlock);
    	this.maxHeightBlock = genesisBlock;
    	
    }

    /** Get the maximum height block */
    public Block getMaxHeightBlock() {
        maxHeightBlock = blockChain.get(blockChain.size()-1);
        return maxHeightBlock; 
    }

    /** Get the UTXOPool for mining a new block on top of max height block */
    public UTXOPool getMaxHeightUTXOPool() {
        // IMPLEMENT THIS
    	UTXOPool maxHeightUTXOPool = new UTXOPool();
    	ArrayList<Transaction> blockTx = maxHeightBlock.getTransactions();
    	for (Transaction tx : blockTx) {
	        for (int i = 0; i < tx.numOutputs(); i++) {
	            Transaction.Output out = tx.getOutput(i);
	            UTXO utxo = new UTXO(tx.getHash(), i);
	            maxHeightUTXOPool.addUTXO(utxo, out);
	        }
    	}
    	return maxHeightUTXOPool;
    }

    /** Get the transaction pool to mine a new block */
    public TransactionPool getTransactionPool() {
        // IMPLEMENT THIS
    	return transactionPool;
    }

    /**
     * Add {@code block} to the block chain if it is valid. For validity, all transactions should be
     * valid and block should be at {@code height > (maxHeight - CUT_OFF_AGE)}.
     * 
     * <p>
     * For example, you can try creating a new block over the genesis block (block height 2) if the
     * block chain height is {@code <=
     * CUT_OFF_AGE + 1}. As soon as {@code height > CUT_OFF_AGE + 1}, you cannot create a new block
     * at height 2.
     * 
     * @return true if block is successfully added
     */
    public boolean addBlock(Block block) {
        // IMPLEMENT THIS
    }

    /** Add a transaction to the transaction pool */
    public void addTransaction(Transaction tx) {
        // IMPLEMENT THIS
    }
}