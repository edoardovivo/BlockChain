import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    UTXOPool maxHeightUTXOPool = null;
     
    TransactionPool transactionPool; 
    
    public BlockChain(Block genesisBlock) {
        // Initialize quantities
    	blockChain = new ArrayList<Block>();
    	maxHeightUTXOPool = new UTXOPool();  	
    	blockChain.add(genesisBlock);
    	transactionPool = new TransactionPool();
    	maxHeightBlock = genesisBlock;
    	maxHeightUTXOPool = getMaxHeightUTXOPool();
    }

    /** Get the maximum height block */
    public Block getMaxHeightBlock() {
    	Block mx = blockChain.get(blockChain.size()-1);
        return mx; 
    }

    /** Get the UTXOPool for mining a new block on top of max height block */
    public UTXOPool getMaxHeightUTXOPool() {
        // IMPLEMENT THIS
    	UTXOPool utxoPool = new UTXOPool();
    	ArrayList<Transaction> blockTx = maxHeightBlock.getTransactions();
    	for (Transaction tx : blockTx) {
	        for (int i = 0; i < tx.numOutputs(); i++) {
	            Transaction.Output out = tx.getOutput(i);
	            UTXO utxo = new UTXO(tx.getHash(), i);
	            utxoPool.addUTXO(utxo, out);
	        }
    	}
    	//Add outputs for the previous block coinbase transaction
    	Transaction coinbase = maxHeightBlock.getCoinbase();
    	for (int i = 0; i < coinbase.numOutputs(); i++) {
            Transaction.Output out = coinbase.getOutput(i);
            UTXO utxo = new UTXO(coinbase.getHash(), i);
            utxoPool.addUTXO(utxo, out);
        }

    	
    	
    	return utxoPool;
    }

    /** Get the transaction pool to mine a new block */
    public TransactionPool getTransactionPool() {
        // IMPLEMENT THIS
    	return this.transactionPool;
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
    public int getBlockHeight(Block block) {
    	byte[] prevBlockHash = block.getPrevBlockHash();
    	int height = 1;
    	for (Block bl : blockChain) {
    		if (bl.getHash() == prevBlockHash) {
    			break;
    		}
    		height += 1;
    	}
    	return height + 1;
    }
    
    public boolean addBlock(Block block) {
        //Previous block hash should be valid
    	byte[] prevBlockHash = block.getPrevBlockHash();
    	boolean anyValidPrevHash = false;
    	if (prevBlockHash == null) return false;
    	for (Block bl : blockChain) {
    		if (bl.getHash() == prevBlockHash) {
    			anyValidPrevHash = true;
    		}
    	}
    	if (!anyValidPrevHash) return false;
    	
    	
    	// All transaction should be valid
    	TxHandler txHandler = new TxHandler(this.maxHeightUTXOPool);
    	ArrayList<Transaction> transactionList = block.getTransactions();
    	Transaction[] validTxs;
    	validTxs = txHandler.handleTxs(
    			transactionList.toArray(new Transaction[transactionList.size()]));
    	
    	Set<Transaction> transactionSet = new HashSet<Transaction>(transactionList);
    	Set<Transaction> validtransactionSet = new HashSet<Transaction>(Arrays.asList(validTxs));
    	
    	if (!transactionSet.equals(validtransactionSet)) {
    		return false;
    	}
    	else {
	    	
	    	//Height of the new block.
	    	int blockHeight = getBlockHeight(block);
	    	int maxHeight = blockChain.size();
	    	if (blockHeight > (maxHeight - CUT_OFF_AGE)) {
	    		blockChain.add(block);
	    		//Remove transactions from transaction Pool
	    		for (Transaction tx : transactionList) {
	    			transactionPool.removeTransaction(tx.getHash());
	    		}
	    		//Add coinbase transaction to pool
	    		transactionPool.addTransaction(block.getCoinbase());
	    		maxHeightBlock = getMaxHeightBlock();
	    		maxHeightUTXOPool = getMaxHeightUTXOPool();
	    		return true;
	    	}
	    	else {
	    		return false;
	    	}
    	}
    	

    	
    }

    /** Add a transaction to the transaction pool */
    public void addTransaction(Transaction tx) {
        // IMPLEMENT THIS
    	transactionPool.addTransaction(tx);
    }
}


