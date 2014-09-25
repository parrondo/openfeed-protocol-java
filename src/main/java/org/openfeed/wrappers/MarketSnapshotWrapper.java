package org.openfeed.wrappers;

import java.util.List;

import org.openfeed.EntryGroup;
import org.openfeed.MarketEntry;
import org.openfeed.MarketSnapshot;
import org.openfeed.MarketSnapshot.Builder;
import org.openfeed.MarketSnapshotOrBuilder;
import org.openfeed.util.datetime.DateOnlyValue;
import org.openfeed.util.datetime.DateTimeValue;
import org.openfeed.util.datetime.ProtoDateUtil;

public class MarketSnapshotWrapper {

	MarketSnapshotOrBuilder message;
	MarketEntries entries;

	/**
	 * Construct a new wrapper with a wrapped message builder.
	 */
	public MarketSnapshotWrapper() {
		this(MarketSnapshot.newBuilder());
	}

	/**
	 * Construct a new wrapper with the given message or builder.
	 */
	public MarketSnapshotWrapper(final MarketSnapshotOrBuilder message_) {
		message = message_;
		entries = new MarketEntries(message);
	}

	/**
	 * Underlying protobuf message. Note that modifying this directly can invalidate some performance caching that this
	 * wrapper does, so you should avoid direct access until you are done with the wrapper.
	 */
	public MarketSnapshot message() {

		if (message instanceof MarketSnapshot)
			return (MarketSnapshot) message;

		return builder().build();

	}

	/**
	 * Get the underlying message as a builder, if possible. Throws IllegalStateException if the underlying message is
	 * already built (and thus read-only).
	 */
	private Builder builder() {

		if (!(message instanceof Builder)) {
			throw new IllegalStateException("Message is not a builder");
		}

		return (Builder) message;

	}

	/**
	 * Globally unique market identifier.
	 */
	public long id() {
		return message.getBaseMarketId();
	}

	public MarketSnapshotWrapper id(final long id) {
		builder().setBaseMarketId(id);
		return this;
	}

	public long sequence() {
		return message.getBaseSequence();
	}

	public MarketSnapshotWrapper sequence(final long sequence) {
		builder().setBaseSequence(sequence);
		return this;
	}

	/**
	 * The timestamp for this message.
	 */
	public DateTimeValue timestamp() {
		return ProtoDateUtil.fromDecimalDateTime(message.getBaseTimeStamp());
	}

	public MarketSnapshotWrapper timestamp(final DateTimeValue timestamp) {
		builder().setBaseTimeStamp(ProtoDateUtil.intoDecimalDateTime(timestamp));
		return this;
	}

	/**
	 * The trade date for this message.
	 */
	public DateOnlyValue tradeDate() {
		return ProtoDateUtil.fromDecimalDateOnly(message.getBaseTradeDate());
	}

	public MarketSnapshotWrapper tradeDate(final DateOnlyValue tradeDate) {
		builder().setBaseTradeDate(ProtoDateUtil.intoDecimalDateOnly(tradeDate));
		return this;
	}

	/**
	 * The default price exponent.
	 */
	public long priceExponent() {
		return message.getBasePriceExponent();
	}

	public MarketSnapshotWrapper priceExponent(final int exponent) {
		builder().setBasePriceExponent(exponent);
		return this;
	}

	/**
	 * The default size exponent.
	 */
	public long sizeExponent() {
		return message.getBaseSizeExponent();
	}

	public MarketSnapshotWrapper sizeExponent(final int exponent) {
		builder().setBaseSizeExponent(exponent);
		return this;
	}

	/**
	 * The last update sequence number.
	 */
	public long lastUpdateSequence() {
		return message.getLastUpdateSequence();
	}

	public MarketSnapshotWrapper lastUpdateSequence(final long sequence) {
		builder().setLastUpdateSequence(sequence);
		return this;
	}

	/**
	 * The number of entries expected for this snapshot (can span multiple messages).
	 */
	public int expectedEntries() {
		return message.getTotalExpectedEntries();
	}

	public MarketSnapshotWrapper expectedEntries(final int expected) {
		builder().setTotalExpectedEntries(expected);
		return this;
	}

	/**
	 * The session group for this snapshot.
	 */
	public EntryGroup group() {
		return message.getEntryGroup();
	}

	public MarketSnapshotWrapper group(final EntryGroup group) {
		builder().setEntryGroup(group);
		return this;
	}

	/**
	 * Get all market entries.
	 */
	public List<MarketEntryWrapper> entries() {
		return null;
	}

	/**
	 * Get the first market entry with the specified type.
	 */
	public MarketEntryWrapper entry(final MarketEntry.Type type) {
		return null;
	}

	/**
	 * Get all market entries with the specified type.
	 */
	public List<MarketEntryWrapper> entries(final MarketEntry.Type type) {
		return null;
	}

	/**
	 * Get the first market entry with the specified type and descriptor.
	 */
	public MarketEntryWrapper entry(final MarketEntry.Type type, final MarketEntry.Descriptor descriptor) {
		return null;
	}

	/**
	 * Get all market entries with the specified type and descriptor.
	 */
	public List<MarketEntryWrapper> entries(final MarketEntry.Type type, final MarketEntry.Descriptor descriptor) {
		return null;
	}

}