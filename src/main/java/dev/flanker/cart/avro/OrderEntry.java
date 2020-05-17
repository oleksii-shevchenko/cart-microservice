/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package dev.flanker.cart.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class OrderEntry extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 6119540885338973932L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"OrderEntry\",\"namespace\":\"dev.flanker.cart.avro\",\"fields\":[{\"name\":\"itemId\",\"type\":\"long\"},{\"name\":\"number\",\"type\":\"int\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<OrderEntry> ENCODER =
      new BinaryMessageEncoder<OrderEntry>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<OrderEntry> DECODER =
      new BinaryMessageDecoder<OrderEntry>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<OrderEntry> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<OrderEntry> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<OrderEntry> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<OrderEntry>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this OrderEntry to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a OrderEntry from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a OrderEntry instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static OrderEntry fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private long itemId;
   private int number;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public OrderEntry() {}

  /**
   * All-args constructor.
   * @param itemId The new value for itemId
   * @param number The new value for number
   */
  public OrderEntry(java.lang.Long itemId, java.lang.Integer number) {
    this.itemId = itemId;
    this.number = number;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return itemId;
    case 1: return number;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: itemId = (java.lang.Long)value$; break;
    case 1: number = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'itemId' field.
   * @return The value of the 'itemId' field.
   */
  public long getItemId() {
    return itemId;
  }


  /**
   * Sets the value of the 'itemId' field.
   * @param value the value to set.
   */
  public void setItemId(long value) {
    this.itemId = value;
  }

  /**
   * Gets the value of the 'number' field.
   * @return The value of the 'number' field.
   */
  public int getNumber() {
    return number;
  }


  /**
   * Sets the value of the 'number' field.
   * @param value the value to set.
   */
  public void setNumber(int value) {
    this.number = value;
  }

  /**
   * Creates a new OrderEntry RecordBuilder.
   * @return A new OrderEntry RecordBuilder
   */
  public static dev.flanker.cart.avro.OrderEntry.Builder newBuilder() {
    return new dev.flanker.cart.avro.OrderEntry.Builder();
  }

  /**
   * Creates a new OrderEntry RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new OrderEntry RecordBuilder
   */
  public static dev.flanker.cart.avro.OrderEntry.Builder newBuilder(dev.flanker.cart.avro.OrderEntry.Builder other) {
    if (other == null) {
      return new dev.flanker.cart.avro.OrderEntry.Builder();
    } else {
      return new dev.flanker.cart.avro.OrderEntry.Builder(other);
    }
  }

  /**
   * Creates a new OrderEntry RecordBuilder by copying an existing OrderEntry instance.
   * @param other The existing instance to copy.
   * @return A new OrderEntry RecordBuilder
   */
  public static dev.flanker.cart.avro.OrderEntry.Builder newBuilder(dev.flanker.cart.avro.OrderEntry other) {
    if (other == null) {
      return new dev.flanker.cart.avro.OrderEntry.Builder();
    } else {
      return new dev.flanker.cart.avro.OrderEntry.Builder(other);
    }
  }

  /**
   * RecordBuilder for OrderEntry instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<OrderEntry>
    implements org.apache.avro.data.RecordBuilder<OrderEntry> {

    private long itemId;
    private int number;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(dev.flanker.cart.avro.OrderEntry.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.itemId)) {
        this.itemId = data().deepCopy(fields()[0].schema(), other.itemId);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.number)) {
        this.number = data().deepCopy(fields()[1].schema(), other.number);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
    }

    /**
     * Creates a Builder by copying an existing OrderEntry instance
     * @param other The existing instance to copy.
     */
    private Builder(dev.flanker.cart.avro.OrderEntry other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.itemId)) {
        this.itemId = data().deepCopy(fields()[0].schema(), other.itemId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.number)) {
        this.number = data().deepCopy(fields()[1].schema(), other.number);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'itemId' field.
      * @return The value.
      */
    public long getItemId() {
      return itemId;
    }


    /**
      * Sets the value of the 'itemId' field.
      * @param value The value of 'itemId'.
      * @return This builder.
      */
    public dev.flanker.cart.avro.OrderEntry.Builder setItemId(long value) {
      validate(fields()[0], value);
      this.itemId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'itemId' field has been set.
      * @return True if the 'itemId' field has been set, false otherwise.
      */
    public boolean hasItemId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'itemId' field.
      * @return This builder.
      */
    public dev.flanker.cart.avro.OrderEntry.Builder clearItemId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'number' field.
      * @return The value.
      */
    public int getNumber() {
      return number;
    }


    /**
      * Sets the value of the 'number' field.
      * @param value The value of 'number'.
      * @return This builder.
      */
    public dev.flanker.cart.avro.OrderEntry.Builder setNumber(int value) {
      validate(fields()[1], value);
      this.number = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'number' field has been set.
      * @return True if the 'number' field has been set, false otherwise.
      */
    public boolean hasNumber() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'number' field.
      * @return This builder.
      */
    public dev.flanker.cart.avro.OrderEntry.Builder clearNumber() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public OrderEntry build() {
      try {
        OrderEntry record = new OrderEntry();
        record.itemId = fieldSetFlags()[0] ? this.itemId : (java.lang.Long) defaultValue(fields()[0]);
        record.number = fieldSetFlags()[1] ? this.number : (java.lang.Integer) defaultValue(fields()[1]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<OrderEntry>
    WRITER$ = (org.apache.avro.io.DatumWriter<OrderEntry>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<OrderEntry>
    READER$ = (org.apache.avro.io.DatumReader<OrderEntry>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeLong(this.itemId);

    out.writeInt(this.number);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.itemId = in.readLong();

      this.number = in.readInt();

    } else {
      for (int i = 0; i < 2; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.itemId = in.readLong();
          break;

        case 1:
          this.number = in.readInt();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










