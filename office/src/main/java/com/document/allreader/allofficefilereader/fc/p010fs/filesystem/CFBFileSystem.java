package com.document.allreader.allofficefilereader.fc.p010fs.filesystem;

import com.document.allreader.allofficefilereader.fc.p010fs.storage.BlockAllocationTableReader;
import com.document.allreader.allofficefilereader.fc.p010fs.storage.BlockList;
import com.document.allreader.allofficefilereader.fc.p010fs.storage.HeaderBlock;
import com.document.allreader.allofficefilereader.fc.p010fs.storage.RawDataBlock;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/* renamed from: com.allreader.office.allofficefilereader.fc.fs.filesystem.CFBFileSystem */

public class CFBFileSystem {
    private BlockSize bigBlockSize;
    private HeaderBlock headerBlock;
    private boolean isGetThumbnail;
    private Property root;

    public CFBFileSystem(InputStream inputStream) throws IOException {
        this(inputStream, false);
    }

    /* JADX INFO: finally extract failed */
    public CFBFileSystem(InputStream inputStream, boolean z) throws IOException {
        this.bigBlockSize = CFBConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
        this.isGetThumbnail = z;
        try {
            HeaderBlock headerBlock = new HeaderBlock(inputStream);
            this.headerBlock = headerBlock;
            BlockSize bigBlockSize = headerBlock.getBigBlockSize();
            this.bigBlockSize = bigBlockSize;
            BlockList blockList = new BlockList(inputStream, bigBlockSize);
            inputStream.close();
            new BlockAllocationTableReader(this.headerBlock.getBigBlockSize(), this.headerBlock.getBATCount(), this.headerBlock.getBATArray(), this.headerBlock.getXBATCount(), this.headerBlock.getXBATIndex(), blockList);
            ArrayList arrayList = new ArrayList();
            readProperties(blockList.fetchBlocks(this.headerBlock.getPropertyStart(), -1), blockList, arrayList);
            createPropertyTree(this.root, arrayList);
            readPrepertiesRawData(readSmallRawDataBlock(blockList), blockList, this.root);
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    private void createPropertyTree(Property property, List<Property> list) {
        int childPropertyIndex = property.getChildPropertyIndex();
        if (childPropertyIndex >= 0) {
            Stack stack = new Stack();
            stack.push(list.get(childPropertyIndex));
            while (!stack.isEmpty()) {
                Property property2 = (Property) stack.pop();
                property.addChildProperty(property2);
                if (property2.isDirectory()) {
                    createPropertyTree(property2, list);
                }
                int previousPropertyIndex = property2.getPreviousPropertyIndex();
                if (previousPropertyIndex >= 0) {
                    stack.push(list.get(previousPropertyIndex));
                }
                int nextPropertyIndex = property2.getNextPropertyIndex();
                if (nextPropertyIndex >= 0) {
                    stack.push(list.get(nextPropertyIndex));
                }
            }
        }
    }

    private void readProperties(RawDataBlock[] rawDataBlockArr, BlockList blockList, List<Property> list) throws IOException {
        for (RawDataBlock rawDataBlock : rawDataBlockArr) {
            byte[] data = rawDataBlock.getData();
            int length = data.length / 128;
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                byte b = data[i + 66];
                if (b == 1) {
                    list.add(new Property(list.size(), data, i));
                } else if (b == 2) {
                    list.add(new Property(list.size(), data, i));
                } else if (b == 5) {
                    Property property = new Property(list.size(), data, i);
                    this.root = property;
                    list.add(property);
                }
                i += 128;
            }
        }
    }

    private BlockList readSmallRawDataBlock(BlockList blockList) throws IOException {
        RawDataBlock[] fetchBlocks = blockList.fetchBlocks(this.root.getStartBlock(), -1);
        int bigBlockSize = this.headerBlock.getBigBlockSize().getBigBlockSize() / 64;
        ArrayList arrayList = new ArrayList();
        for (RawDataBlock rawDataBlock : fetchBlocks) {
            byte[] data = rawDataBlock.getData();
            for (int i = 0; i < bigBlockSize; i++) {
                byte[] bArr = new byte[64];
                System.arraycopy(data, i * 64, bArr, 0, 64);
                arrayList.add(new RawDataBlock(bArr));
            }
        }
        BlockList blockList2 = new BlockList((RawDataBlock[]) arrayList.toArray(new RawDataBlock[arrayList.size()]));
        new BlockAllocationTableReader(this.bigBlockSize, blockList.fetchBlocks(this.headerBlock.getSBATStart(), -1), blockList2);
        return blockList2;
    }

    private void readPrepertiesRawData(BlockList blockList, BlockList blockList2, Property property) throws IOException {
        for (Property property2 : property.properties.values()) {
            if (property2.isDocument()) {
                getPropertyRawData(property2, blockList, blockList2);
            } else if (property2.isDirectory()) {
                readPrepertiesRawData(blockList, blockList2, property2);
            }
        }
    }

    private void getPropertyRawData(Property property, BlockList blockList, BlockList blockList2) throws IOException {
        RawDataBlock[] rawDataBlockArr;
        String name = property.getName();
        int startBlock = property.getStartBlock();
        if (property.shouldUseSmallBlocks()) {
            rawDataBlockArr = blockList.fetchBlocks(startBlock, this.headerBlock.getPropertyStart());
        } else {
            rawDataBlockArr = blockList2.fetchBlocks(startBlock, this.headerBlock.getPropertyStart());
        }
        if (!(rawDataBlockArr == null || rawDataBlockArr.length == 0)) {
            if (name.equals("Pictures") || name.endsWith("WorkBook") || name.equals("PowerPoint Document") || name.endsWith("Ole") || name.endsWith("ObjInfo") || name.endsWith("ComObj") || name.endsWith("EPRINT")) {
                property.setBlocks(rawDataBlockArr);
                return;
            }
            int length = rawDataBlockArr[0].getData().length;
            byte[] bArr = new byte[(rawDataBlockArr.length * length)];
            int i = 0;
            for (RawDataBlock rawDataBlock : rawDataBlockArr) {
                System.arraycopy(rawDataBlock.getData(), 0, bArr, i, length);
                i += length;
            }
            property.setDocumentRawData(bArr);
        }
    }

    public byte[] getPropertyRawData(String str) {
        Property property = getProperty(str);
        if (property != null) {
            return property.getDocumentRawData();
        }
        return null;
    }

    public Property getProperty(String str) {
        return this.root.getChlidProperty(str);
    }

    public void dispose() {
        HeaderBlock headerBlock = this.headerBlock;
        if (headerBlock != null) {
            headerBlock.dispose();
            this.headerBlock = null;
        }
        Property property = this.root;
        if (property != null) {
            property.dispose();
        }
    }
}
