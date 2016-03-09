/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package ch.bfh.progressor.executor.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2016-03-09")
public class PerformanceIndicators implements org.apache.thrift.TBase<PerformanceIndicators, PerformanceIndicators._Fields>, java.io.Serializable, Cloneable, Comparable<PerformanceIndicators> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PerformanceIndicators");

  private static final org.apache.thrift.protocol.TField RUNTIME_MILLISECONDS_FIELD_DESC = new org.apache.thrift.protocol.TField("runtimeMilliseconds", org.apache.thrift.protocol.TType.DOUBLE, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new PerformanceIndicatorsStandardSchemeFactory());
    schemes.put(TupleScheme.class, new PerformanceIndicatorsTupleSchemeFactory());
  }

  public double runtimeMilliseconds; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    RUNTIME_MILLISECONDS((short)1, "runtimeMilliseconds");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // RUNTIME_MILLISECONDS
          return RUNTIME_MILLISECONDS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __RUNTIMEMILLISECONDS_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.RUNTIME_MILLISECONDS, new org.apache.thrift.meta_data.FieldMetaData("runtimeMilliseconds", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PerformanceIndicators.class, metaDataMap);
  }

  public PerformanceIndicators() {
  }

  public PerformanceIndicators(
    double runtimeMilliseconds)
  {
    this();
    this.runtimeMilliseconds = runtimeMilliseconds;
    setRuntimeMillisecondsIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PerformanceIndicators(PerformanceIndicators other) {
    __isset_bitfield = other.__isset_bitfield;
    this.runtimeMilliseconds = other.runtimeMilliseconds;
  }

  public PerformanceIndicators deepCopy() {
    return new PerformanceIndicators(this);
  }

  @Override
  public void clear() {
    setRuntimeMillisecondsIsSet(false);
    this.runtimeMilliseconds = 0.0;
  }

  public double getRuntimeMilliseconds() {
    return this.runtimeMilliseconds;
  }

  public PerformanceIndicators setRuntimeMilliseconds(double runtimeMilliseconds) {
    this.runtimeMilliseconds = runtimeMilliseconds;
    setRuntimeMillisecondsIsSet(true);
    return this;
  }

  public void unsetRuntimeMilliseconds() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __RUNTIMEMILLISECONDS_ISSET_ID);
  }

  /** Returns true if field runtimeMilliseconds is set (has been assigned a value) and false otherwise */
  public boolean isSetRuntimeMilliseconds() {
    return EncodingUtils.testBit(__isset_bitfield, __RUNTIMEMILLISECONDS_ISSET_ID);
  }

  public void setRuntimeMillisecondsIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __RUNTIMEMILLISECONDS_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case RUNTIME_MILLISECONDS:
      if (value == null) {
        unsetRuntimeMilliseconds();
      } else {
        setRuntimeMilliseconds((Double)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case RUNTIME_MILLISECONDS:
      return getRuntimeMilliseconds();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case RUNTIME_MILLISECONDS:
      return isSetRuntimeMilliseconds();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof PerformanceIndicators)
      return this.equals((PerformanceIndicators)that);
    return false;
  }

  public boolean equals(PerformanceIndicators that) {
    if (that == null)
      return false;

    boolean this_present_runtimeMilliseconds = true;
    boolean that_present_runtimeMilliseconds = true;
    if (this_present_runtimeMilliseconds || that_present_runtimeMilliseconds) {
      if (!(this_present_runtimeMilliseconds && that_present_runtimeMilliseconds))
        return false;
      if (this.runtimeMilliseconds != that.runtimeMilliseconds)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_runtimeMilliseconds = true;
    list.add(present_runtimeMilliseconds);
    if (present_runtimeMilliseconds)
      list.add(runtimeMilliseconds);

    return list.hashCode();
  }

  @Override
  public int compareTo(PerformanceIndicators other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetRuntimeMilliseconds()).compareTo(other.isSetRuntimeMilliseconds());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRuntimeMilliseconds()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.runtimeMilliseconds, other.runtimeMilliseconds);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("PerformanceIndicators(");
    boolean first = true;

    sb.append("runtimeMilliseconds:");
    sb.append(this.runtimeMilliseconds);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class PerformanceIndicatorsStandardSchemeFactory implements SchemeFactory {
    public PerformanceIndicatorsStandardScheme getScheme() {
      return new PerformanceIndicatorsStandardScheme();
    }
  }

  private static class PerformanceIndicatorsStandardScheme extends StandardScheme<PerformanceIndicators> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PerformanceIndicators struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // RUNTIME_MILLISECONDS
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.runtimeMilliseconds = iprot.readDouble();
              struct.setRuntimeMillisecondsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, PerformanceIndicators struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(RUNTIME_MILLISECONDS_FIELD_DESC);
      oprot.writeDouble(struct.runtimeMilliseconds);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class PerformanceIndicatorsTupleSchemeFactory implements SchemeFactory {
    public PerformanceIndicatorsTupleScheme getScheme() {
      return new PerformanceIndicatorsTupleScheme();
    }
  }

  private static class PerformanceIndicatorsTupleScheme extends TupleScheme<PerformanceIndicators> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PerformanceIndicators struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetRuntimeMilliseconds()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetRuntimeMilliseconds()) {
        oprot.writeDouble(struct.runtimeMilliseconds);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PerformanceIndicators struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.runtimeMilliseconds = iprot.readDouble();
        struct.setRuntimeMillisecondsIsSet(true);
      }
    }
  }

}

