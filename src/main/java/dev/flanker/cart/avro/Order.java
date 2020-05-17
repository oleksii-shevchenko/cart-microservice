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
public class Order extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -2476488211790914083L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Order\",\"namespace\":\"dev.flanker.cart.avro\",\"fields\":[{\"name\":\"userId\",\"type\":\"long\"},{\"name\":\"cartId\",\"type\":\"long\"},{\"name\":\"items\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"OrderEntry\",\"fields\":[{\"name\":\"itemId\",\"type\":\"long\"},{\"name\":\"number\",\"type\":\"int\"}]}}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Order> ENCODER =
      new BinaryMessageEncoder<Order>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Order> DECODER =
      new BinaryMessageDecoder<Order>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<Order> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<Order> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<Order> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Order>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this Order to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a Order from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a Order instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static Order fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private long userId;
   private long cartId;
   private java.util.List<dev.flanker.cart.avro.OrderEntry> items;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Order() {}

  /**
   * All-args constructor.
   * @param userId The new value for userId
   * @param cartId The new value for cartId
   * @param items The new value for items
   */
  public Order(java.lang.Long userId, java.lang.Long cartId, java.util.List<dev.flanker.cart.avro.OrderEntry> items) {
    this.userId = userId;
    this.cartId = cartId;
    this.items = items;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return userId;
    case 1: return cartId;
    case 2: return items;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: userId = (java.lang.Long)value$; break;
    case 1: cartId = (java.lang.Long)value$; break;
    case 2: items = (java.util.List<dev.flanker.cart.avro.OrderEntry>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'userId' field.
   * @return The value of the 'userId' field.
   */
  public long getUserId() {
    return userId;
  }


  /**
   * Sets the value of the 'userId' field.
   * @param value the value to set.
   */
  public void setUserId(long value) {
    this.userId = value;
  }

  /**
   * Gets the value of the 'cartId' field.
   * @return The value of the 'cartId' field.
   */
  public long getCartId() {
    return cartId;
  }


  /**
   * Sets the value of the 'cartId' field.
   * @param value the value to set.
   */
  public void setCartId(long value) {
    this.cartId = value;
  }

  /**
   * Gets the value of the 'items' field.
   * @return The value of the 'items' field.
   */
  public java.util.List<dev.flanker.cart.avro.OrderEntry> getItems() {
    return items;
  }


  /**
   * Sets the value of the 'items' field.
   * @param value the value to set.
   */
  public void setItems(java.util.List<dev.flanker.cart.avro.OrderEntry> value) {
    this.items = value;
  }

  /**
   * Creates a new Order RecordBuilder.
   * @return A new Order RecordBuilder
   */
  public static dev.flanker.cart.avro.Order.Builder newBuilder() {
    return new dev.flanker.cart.avro.Order.Builder();
  }

  /**
   * Creates a new Order RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Order RecordBuilder
   */
  public static dev.flanker.cart.avro.Order.Builder newBuilder(dev.flanker.cart.avro.Order.Builder other) {
    if (other == null) {
      return new dev.flanker.cart.avro.Order.Builder();
    } else {
      return new dev.flanker.cart.avro.Order.Builder(other);
    }
  }

  /**
   * Creates a new Order RecordBuilder by copying an existing Order instance.
   * @param other The existing instance to copy.
   * @return A new Order RecordBuilder
   */
  public static dev.flanker.cart.avro.Order.Builder newBuilder(dev.flanker.cart.avro.Order other) {
    if (other == null) {
      return new dev.flanker.cart.avro.Order.Builder();
    } else {
      return new dev.flanker.cart.avro.Order.Builder(other);
    }
  }

  /**
   * RecordBuilder for Order instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Order>
    implements org.apache.avro.data.RecordBuilder<Order> {

    private long userId;
    private long cartId;
    private java.util.List<dev.flanker.cart.avro.OrderEntry> items;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(dev.flanker.cart.avro.Order.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.userId)) {
        this.userId = data().deepCopy(fields()[0].schema(), other.userId);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.cartId)) {
        this.cartId = data().deepCopy(fields()[1].schema(), other.cartId);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.items)) {
        this.items = data().deepCopy(fields()[2].schema(), other.items);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
    }

    /**
     * Creates a Builder by copying an existing Order instance
     * @param other The existing instance to copy.
     */
    private Builder(dev.flanker.cart.avro.Order other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.userId)) {
        this.userId = data().deepCopy(fields()[0].schema(), other.userId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.cartId)) {
        this.cartId = data().deepCopy(fields()[1].schema(), other.cartId);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.items)) {
        this.items = data().deepCopy(fields()[2].schema(), other.items);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'userId' field.
      * @return The value.
      */
    public long getUserId() {
      return userId;
    }


    /**
      * Sets the value of the 'userId' field.
      * @param value The value of 'userId'.
      * @return This builder.
      */
    public dev.flanker.cart.avro.Order.Builder setUserId(long value) {
      validate(fields()[0], value);
      this.userId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'userId' field has been set.
      * @return True if the 'userId' field has been set, false otherwise.
      */
    public boolean hasUserId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'userId' field.
      * @return This builder.
      */
    public dev.flanker.cart.avro.Order.Builder clearUserId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'cartId' field.
      * @return The value.
      */
    public long getCartId() {
      return cartId;
    }


    /**
      * Sets the value of the 'cartId' field.
      * @param value The value of 'cartId'.
      * @return This builder.
      */
    public dev.flanker.cart.avro.Order.Builder setCartId(long value) {
      validate(fields()[1], value);
      this.cartId = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'cartId' field has been set.
      * @return True if the 'cartId' field has been set, false otherwise.
      */
    public boolean hasCartId() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'cartId' field.
      * @return This builder.
      */
    public dev.flanker.cart.avro.Order.Builder clearCartId() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'items' field.
      * @return The value.
      */
    public java.util.List<dev.flanker.cart.avro.OrderEntry> getItems() {
      return items;
    }


    /**
      * Sets the value of the 'items' field.
      * @param value The value of 'items'.
      * @return This builder.
      */
    public dev.flanker.cart.avro.Order.Builder setItems(java.util.List<dev.flanker.cart.avro.OrderEntry> value) {
      validate(fields()[2], value);
      this.items = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'items' field has been set.
      * @return True if the 'items' field has been set, false otherwise.
      */
    public boolean hasItems() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'items' field.
      * @return This builder.
      */
    public dev.flanker.cart.avro.Order.Builder clearItems() {
      items = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Order build() {
      try {
        Order record = new Order();
        record.userId = fieldSetFlags()[0] ? this.userId : (java.lang.Long) defaultValue(fields()[0]);
        record.cartId = fieldSetFlags()[1] ? this.cartId : (java.lang.Long) defaultValue(fields()[1]);
        record.items = fieldSetFlags()[2] ? this.items : (java.util.List<dev.flanker.cart.avro.OrderEntry>) defaultValue(fields()[2]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Order>
    WRITER$ = (org.apache.avro.io.DatumWriter<Order>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Order>
    READER$ = (org.apache.avro.io.DatumReader<Order>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeLong(this.userId);

    out.writeLong(this.cartId);

    long size0 = this.items.size();
    out.writeArrayStart();
    out.setItemCount(size0);
    long actualSize0 = 0;
    for (dev.flanker.cart.avro.OrderEntry e0: this.items) {
      actualSize0++;
      out.startItem();
      e0.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize0 != size0)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size0 + ", but element count was " + actualSize0 + ".");

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.userId = in.readLong();

      this.cartId = in.readLong();

      long size0 = in.readArrayStart();
      java.util.List<dev.flanker.cart.avro.OrderEntry> a0 = this.items;
      if (a0 == null) {
        a0 = new SpecificData.Array<dev.flanker.cart.avro.OrderEntry>((int)size0, SCHEMA$.getField("items").schema());
        this.items = a0;
      } else a0.clear();
      SpecificData.Array<dev.flanker.cart.avro.OrderEntry> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<dev.flanker.cart.avro.OrderEntry>)a0 : null);
      for ( ; 0 < size0; size0 = in.arrayNext()) {
        for ( ; size0 != 0; size0--) {
          dev.flanker.cart.avro.OrderEntry e0 = (ga0 != null ? ga0.peek() : null);
          if (e0 == null) {
            e0 = new dev.flanker.cart.avro.OrderEntry();
          }
          e0.customDecode(in);
          a0.add(e0);
        }
      }

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.userId = in.readLong();
          break;

        case 1:
          this.cartId = in.readLong();
          break;

        case 2:
          long size0 = in.readArrayStart();
          java.util.List<dev.flanker.cart.avro.OrderEntry> a0 = this.items;
          if (a0 == null) {
            a0 = new SpecificData.Array<dev.flanker.cart.avro.OrderEntry>((int)size0, SCHEMA$.getField("items").schema());
            this.items = a0;
          } else a0.clear();
          SpecificData.Array<dev.flanker.cart.avro.OrderEntry> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<dev.flanker.cart.avro.OrderEntry>)a0 : null);
          for ( ; 0 < size0; size0 = in.arrayNext()) {
            for ( ; size0 != 0; size0--) {
              dev.flanker.cart.avro.OrderEntry e0 = (ga0 != null ? ga0.peek() : null);
              if (e0 == null) {
                e0 = new dev.flanker.cart.avro.OrderEntry();
              }
              e0.customDecode(in);
              a0.add(e0);
            }
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










