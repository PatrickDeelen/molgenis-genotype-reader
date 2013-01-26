package org.molgenis.genotype.tabix;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.samtools.util.BlockCompressedInputStream;

import org.apache.commons.io.IOUtils;
import org.molgenis.genotype.GenotypeDataIndex;

public class TabixIndex implements GenotypeDataIndex
{
	private String[] seqNames;// distinct #CHROM column

	private int mPreset;
	private int mSc;
	private int mBc;
	private int mEc;
	private int mMeta;
	private int mSkip;
	private TIndex[] mIndex;
	private HashMap<String, Integer> mChr2tid;

	public TabixIndex(File tabixIndexFile) throws IOException
	{
		readIndexFile(tabixIndexFile);
	}

	public List<String> getSeqNames()
	{
		return Collections.unmodifiableList(Arrays.asList(seqNames));
	}

	private void readIndexFile(File tabixIndexFile) throws IOException
	{
		BlockCompressedInputStream bciStream = new BlockCompressedInputStream(tabixIndexFile);

		try
		{
			byte[] buf = new byte[4];
			bciStream.read(buf, 0, 4); // read "TBI\1"
			seqNames = new String[readInt(bciStream)]; // # sequences
			mChr2tid = new HashMap<String, Integer>();
			mPreset = readInt(bciStream);
			mSc = readInt(bciStream);
			mBc = readInt(bciStream);
			mEc = readInt(bciStream);
			mMeta = readInt(bciStream);
			mSkip = readInt(bciStream);
			// read sequence dictionary
			int i, j, k, l = readInt(bciStream);
			buf = new byte[l];
			bciStream.read(buf);
			for (i = j = k = 0; i < buf.length; ++i)
			{
				if (buf[i] == 0)
				{
					byte[] b = new byte[i - j];
					System.arraycopy(buf, j, b, 0, b.length);
					String s = new String(b);
					mChr2tid.put(s, k);
					seqNames[k++] = s;
					j = i + 1;
				}
			}
			// read the index
			mIndex = new TIndex[seqNames.length];
			for (i = 0; i < seqNames.length; ++i)
			{
				// the binning index
				int n_bin = readInt(bciStream);
				mIndex[i] = new TIndex();
				mIndex[i].b = new HashMap<Integer, TPair64[]>();
				for (j = 0; j < n_bin; ++j)
				{
					int bin = readInt(bciStream);
					TPair64[] chunks = new TPair64[readInt(bciStream)];
					for (k = 0; k < chunks.length; ++k)
					{
						long u = readLong(bciStream);
						long v = readLong(bciStream);
						chunks[k] = new TPair64(u, v); // in C, this is
														// inefficient
					}
					mIndex[i].b.put(bin, chunks);
				}
				// the linear index
				mIndex[i].l = new long[readInt(bciStream)];
				for (k = 0; k < mIndex[i].l.length; ++k)
					mIndex[i].l[k] = readLong(bciStream);
			}
		}
		finally
		{
			IOUtils.closeQuietly(bciStream);
		}
	}

	private int readInt(InputStream in) throws IOException
	{
		byte[] buf = new byte[4];
		in.read(buf);

		return ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	private long readLong(InputStream in) throws IOException
	{
		byte[] buf = new byte[8];
		in.read(buf);

		return ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getLong();
	}

	private class TPair64 implements Comparable<TPair64>
	{
		long u, v;

		public TPair64(final long _u, final long _v)
		{
			u = _u;
			v = _v;
		}

		public int compareTo(final TPair64 p)
		{
			return u == p.u ? 0 : ((u < p.u) ^ (u < 0) ^ (p.u < 0)) ? -1 : 1; // unsigned
																				// 64-bit
																				// comparison
		}
	};

	private class TIndex
	{
		HashMap<Integer, TPair64[]> b; // binning index
		long[] l; // linear index
	};
}
