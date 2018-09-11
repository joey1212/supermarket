package com.joey;
import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey reciepient;
    public float value;
    public String parentTransactionId;

    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
    }


    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TransactionOutput [id=");
		builder.append(id);
		builder.append(", reciepient=");
		builder.append(reciepient);
		builder.append(", value=");
		builder.append(value);
		builder.append(", parentTransactionId=");
		builder.append(parentTransactionId);
		builder.append("]");
		return builder.toString();
	}
    
}