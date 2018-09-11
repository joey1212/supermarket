package com.joey;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;

public class Transaction {
	public String transactionId;
	public PublicKey sender;
	public PublicKey reciepient;
	public float value;
	public byte[] signature;

	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

	private static int sequence = 0;

	public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}

	private String calulateHash() {
		sequence++;
		return StringUtil.applySha256(StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient)
				+ Float.toString(value) + sequence);
	}

	public void generateSignature(PrivateKey privateKey) {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient)
				+ Float.toString(value);
		signature = StringUtil.applyECDSASig(privateKey, data);
	}

	public boolean verifySignature() {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient)
				+ Float.toString(value);
		return StringUtil.verifyECDSASig(sender, data, signature);
	}

	public boolean processTransaction() {

		if (verifySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}

		for (TransactionInput i : inputs) {
			i.UTXO = Chain.UTXOs.get(i.transactionOutputId);
		}

		if (getInputsValue() < Chain.minimumTransaction) {
			System.out.println("Transaction Inputs too small: " + getInputsValue());
			System.out.println("Please enter the amount greater than " + Chain.minimumTransaction);
			return false;
		}

		float leftOver = getInputsValue() - value;
		transactionId = calulateHash();
		outputs.add(new TransactionOutput(this.reciepient, value, transactionId));
		outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

		for (TransactionOutput o : outputs) {
			Chain.UTXOs.put(o.id, o);
		}

		for (TransactionInput i : inputs) {
			if (i.UTXO == null)
				continue;
			Chain.UTXOs.remove(i.UTXO.id);
		}

		return true;
	}

	public float getInputsValue() {
		float total = 0;
		for (TransactionInput i : inputs) {
			if (i.UTXO == null)
				continue;
			total += i.UTXO.value;
		}
		return total;
	}

	public float getOutputsValue() {
		float total = 0;
		for (TransactionOutput o : outputs) {
			total += o.value;
		}
		return total;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transaction [transactionId=");
		builder.append(transactionId);
		builder.append(", sender=");
		builder.append(sender);
		builder.append(", reciepient=");
		builder.append(reciepient);
		builder.append(", value=");
		builder.append(value);
		builder.append(", signature=");
		builder.append(Arrays.toString(signature));
		builder.append(", inputs=");
		builder.append(inputs);
		builder.append(", outputs=");
		builder.append(outputs);
		builder.append("]");
		return builder.toString();
	}
	
}