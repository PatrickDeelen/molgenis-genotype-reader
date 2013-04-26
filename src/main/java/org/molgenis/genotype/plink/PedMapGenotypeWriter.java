package org.molgenis.genotype.plink;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.molgenis.genotype.GenotypeData;
import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.VariantAlleles;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.GeneticVariant.Type;
import org.molgenis.util.plink.datatypes.Biallele;
import org.molgenis.util.plink.datatypes.MapEntry;
import org.molgenis.util.plink.datatypes.PedEntry;
import org.molgenis.util.plink.writers.MapFileWriter;
import org.molgenis.util.plink.writers.PedFileWriter;

public class PedMapGenotypeWriter
{
	private static Logger LOG = Logger.getLogger(PedMapGenotypeWriter.class);
	private GenotypeData genotypeData;
	private String fatherSampleAnnotionId;
	private String motherSampleAnnotionId;
	private String sexSampleAnnotionId;
	private String phenoSampleAnnotionId;

	public PedMapGenotypeWriter(GenotypeData genotypeData, String fatherSampleAnnotionId,
			String motherSampleAnnotionId, String sexSampleAnnotionId, String phenoSampleAnnotionId)
	{
		this.genotypeData = genotypeData;
		this.fatherSampleAnnotionId = fatherSampleAnnotionId;
		this.motherSampleAnnotionId = motherSampleAnnotionId;
		this.sexSampleAnnotionId = sexSampleAnnotionId;
		this.phenoSampleAnnotionId = phenoSampleAnnotionId;
	}

	public void write(File pedFile, File mapFile) throws IOException
	{
		writeMapFile(mapFile);
		writePedFile(pedFile);
	}

	@SuppressWarnings("resource")
	private void writeMapFile(File mapFile) throws IOException
	{
		LOG.info("Going to create [" + mapFile + "]");
		MapFileWriter writer = null;
		try
		{
			writer = new MapFileWriter(mapFile);

			int total = genotypeData.getVariants().size();
			int count = 0;

			for (GeneticVariant variant : genotypeData.getVariants())
			{
				if (variant.getType() != Type.SNP)
				{
					throw new GenotypeDataException("Variant [" + variant.getPrimaryVariantId() + "] is not a snp");
				}

				MapEntry mapEntry = new MapEntry(variant.getSequenceName(), variant.getPrimaryVariantId(), 0,
						variant.getStartPos());
				writer.write(mapEntry);
				count++;
				LOG.info("Written " + count + "/" + total + " snps");
			}

		}
		finally
		{
			IOUtils.closeQuietly(writer);
		}
	}

	private void writePedFile(File pedFile) throws IOException
	{
		LOG.info("Going to create [" + pedFile + "]");

		PedFileWriter writer = null;
		try
		{
			writer = new PedFileWriter(pedFile);
			final List<Sample> samples = genotypeData.getSamples();
			int count = samples.size();

			for (int i = 0; i < count; i++)
			{
				Sample sample = samples.get(i);

				PedEntry pedEntry = new PedEntry(getFamilyId(sample), sample.getId(), getFather(sample),
						getMother(sample), getSex(sample), getPhenotype(sample), new BialleleIterator(
								genotypeData.getVariants(), i));

				writer.write(pedEntry);

				LOG.info("Written " + (i + 1) + "/" + count + " samples");
			}

		}
		finally
		{
			IOUtils.closeQuietly(writer);
		}

	}

	private String getFamilyId(Sample sample)
	{
		return sample.getFamilyId() != null ? sample.getFamilyId() : "0";
	}

	private String getFather(Sample sample)
	{
		if (fatherSampleAnnotionId == null)
		{
			return "0";
		}

		Object value = sample.getAnnotations().get(fatherSampleAnnotionId);
		if (value == null)
		{
			return "0";
		}

		return value.toString();
	}

	private String getMother(Sample sample)
	{
		if (motherSampleAnnotionId == null)
		{
			return "0";
		}

		Object value = sample.getAnnotations().get(motherSampleAnnotionId);
		if (value == null)
		{
			return "0";
		}

		return value.toString();
	}

	private byte getSex(Sample sample)
	{
		if (sexSampleAnnotionId == null)
		{
			return 0;
		}

		Object value = sample.getAnnotations().get(sexSampleAnnotionId);
		if (value == null)
		{
			return 0;
		}

		if (value instanceof Byte)
		{
			return (Byte) value;
		}

		return Byte.valueOf(value.toString());
	}

	private double getPhenotype(Sample sample)
	{
		if (phenoSampleAnnotionId == null)
		{
			return -9;
		}

		Object value = sample.getAnnotations().get(phenoSampleAnnotionId);
		if (value == null)
		{
			return -9;
		}

		if (value instanceof Double)
		{
			return (Double) value;
		}

		return Double.valueOf(value.toString());
	}

	private class BialleleIterator implements Iterator<Biallele>
	{
		private Iterator<GeneticVariant> variantsIterator;
		private int sampleIndex;

		public BialleleIterator(List<GeneticVariant> variants, int sampleIndex)
		{
			this.variantsIterator = variants.iterator();
			this.sampleIndex = sampleIndex;
		}

		@Override
		public boolean hasNext()
		{
			return variantsIterator.hasNext();
		}

		@Override
		public Biallele next()
		{
			GeneticVariant variant = variantsIterator.next();
			VariantAlleles variantAlleles = variant.getSampleVariants().get(sampleIndex);
			char[] alleles = variantAlleles.getAllelesAsChars();

			return Biallele.create(alleles[0], alleles[1]);
		}

		@Override
		public void remove()
		{
		}

	}
}
