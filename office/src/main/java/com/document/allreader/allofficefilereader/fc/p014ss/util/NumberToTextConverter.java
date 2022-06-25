package com.document.allreader.allofficefilereader.fc.p014ss.util;



public final class NumberToTextConverter {
    private static final long EXCEL_NAN_BITS = -276939487313920L;
    private static final int MAX_TEXT_LEN = 20;

    private static boolean needsScientificNotation(int i) {
        return i > 20;
    }

    private NumberToTextConverter() {
    }

    public static String toText(double d) {
        return rawDoubleBitsToText(Double.doubleToLongBits(d));
    }

    static String rawDoubleBitsToText(long j) {
        boolean z = false;
        boolean z2 = j < 0;
        if (z2) {
            j &= 9223372036854775807L;
        }
        if (j == 0) {
            return z2 ? "-0" : "0";
        }
        ExpandedDouble expandedDouble = new ExpandedDouble(j);
        if (expandedDouble.getBinaryExponent() < -1022) {
            return z2 ? "-0" : "0";
        }
        if (expandedDouble.getBinaryExponent() != 1024) {
            z = z2;
        } else if (j == EXCEL_NAN_BITS) {
            return "3.484840871308E+308";
        }
        NormalisedDecimal normaliseBaseTen = expandedDouble.normaliseBaseTen();
        StringBuilder sb = new StringBuilder(21);
        if (z) {
            sb.append('-');
        }
        convertToText(sb, normaliseBaseTen);
        return sb.toString();
    }

    private static void convertToText(StringBuilder sb, NormalisedDecimal normalisedDecimal) {
        String str;
        NormalisedDecimal roundUnits = normalisedDecimal.roundUnits();
        int decimalExponent = roundUnits.getDecimalExponent();
        if (Math.abs(decimalExponent) > 98) {
            str = roundUnits.getSignificantDecimalDigitsLastDigitRounded();
            if (str.length() == 16) {
                decimalExponent++;
            }
        } else {
            str = roundUnits.getSignificantDecimalDigits();
        }
        int countSignifantDigits = countSignifantDigits(str);
        if (decimalExponent < 0) {
            formatLessThanOne(sb, str, decimalExponent, countSignifantDigits);
        } else {
            formatGreaterThanOne(sb, str, decimalExponent, countSignifantDigits);
        }
    }

    private static void formatLessThanOne(StringBuilder sb, String str, int i, int i2) {
        int i3 = -i;
        int i4 = i3 - 1;
        if (needsScientificNotation(i4 + 2 + i2)) {
            sb.append(str.charAt(0));
            if (i2 > 1) {
                sb.append('.');
                sb.append(str.subSequence(1, i2));
            }
            sb.append("E-");
            appendExp(sb, i3);
            return;
        }
        sb.append("0.");
        while (i4 > 0) {
            sb.append('0');
            i4--;
        }
        sb.append(str.subSequence(0, i2));
    }

    private static void formatGreaterThanOne(StringBuilder sb, String str, int i, int i2) {
        if (i > 19) {
            sb.append(str.charAt(0));
            if (i2 > 1) {
                sb.append('.');
                sb.append(str.subSequence(1, i2));
            }
            sb.append("E+");
            appendExp(sb, i);
            return;
        }
        int i3 = (i2 - i) - 1;
        if (i3 > 0) {
            int i4 = i + 1;
            sb.append(str.subSequence(0, i4));
            sb.append('.');
            sb.append(str.subSequence(i4, i2));
            return;
        }
        sb.append(str.subSequence(0, i2));
        for (int i5 = -i3; i5 > 0; i5--) {
            sb.append('0');
        }
    }

    private static int countSignifantDigits(String str) {
        int length = str.length() - 1;
        while (str.charAt(length) == '0') {
            length--;
            if (length < 0) {
                throw new RuntimeException("No non-zero digits found");
            }
        }
        return length + 1;
    }

    private static void appendExp(StringBuilder sb, int i) {
        if (i < 10) {
            sb.append('0');
            sb.append((char) (i + 48));
            return;
        }
        sb.append(i);
    }
}
