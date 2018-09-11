package com.joey;

import java.util.ArrayList;

public class Block {
	public String hash;// 本次块hash
	public String previousHash;// 上个块hash
	private String data; // 交易
	private long timeStamp; // as number of milliseconds since 1/1/1970.
	private int nonce;// 工作量证明
	private String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<>();

	// Block Constructor.
	public Block(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = System.currentTimeMillis();
		this.hash = calculateHash();
	}

	public Block(String data, String previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = System.currentTimeMillis();
		this.hash = calculateHash();
	}

	public String calculateHash() {
		String calculatedhash = StringUtil
				.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data);
		return calculatedhash;
	}

	public void mineBlock(int difficulty) {// difficulty 难度
		String target = new String(new char[difficulty]).replace('\0', '0');
		while (!hash.substring(0, difficulty).equals(target)) {
			nonce++;
			hash = calculateHash();
		}
		System.out.println("挖到矿啦!!!" + hash);
	}

	public boolean addTransaction(Transaction transaction) {
		if (transaction == null)
			return false;
		if ((previousHash != "0")) {
			if ((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Block [hash=");
		builder.append(hash);
		builder.append(", previousHash=");
		builder.append(previousHash);
		builder.append(", data=");
		builder.append(data);
		builder.append(", timeStamp=");
		builder.append(timeStamp);
		builder.append(", nonce=");
		builder.append(nonce);
		builder.append(", merkleRoot=");
		builder.append(merkleRoot);
		builder.append(", transactions=");
		builder.append(transactions);
		builder.append("]");
		return builder.toString();
	}
	
}