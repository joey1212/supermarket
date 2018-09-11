package com.joey;
public class TransactionInput {
    public String transactionOutputId;
    public TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TransactionInput [transactionOutputId=");
		builder.append(transactionOutputId);
		builder.append(", UTXO=");
		builder.append(UTXO);
		builder.append("]");
		return builder.toString();
	}
    
}