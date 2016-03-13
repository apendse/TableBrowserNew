package dbtable.aap.tablebrowser;

import android.test.mock.MockCursor;

/**
 * A basic cursor that mocks some simple methods so that we
 * can test a cursor based {@link android.support.v7.widget.RecyclerView.Adapter}
 */
class MyMockCursor extends MockCursor {

    public static final String DUMMY_STRING = "Dummy1234";
    public static final int DUMMY_INT = 9876543;
    public static final float DUMMY_FLOAT = (float) Math.PI;
    public static final String INT_COLUMN = "Int_Column";
    public static final String STRING_COLUMN = "String_Column";
    public static final String FLOAT_COLUMN = "Float_Column";

    final int size;
    final int columns;

    MyMockCursor(int count, int columns) {
        this.size = count;
        this.columns = columns;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public boolean moveToFirst() {
        return true;
    }

    @Override
    public boolean moveToPosition(int position) {
        return true;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return INT_COLUMN;
            case 1:
                return STRING_COLUMN;
            default:
                return FLOAT_COLUMN;
        }
    }

    @Override
    public int getType(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return FIELD_TYPE_INTEGER;
            case 1:
                return FIELD_TYPE_STRING;
            default:
                return FIELD_TYPE_FLOAT;
        }
    }

    @Override
    public String getString(int columnIndex) {
        return DUMMY_STRING;
    }

    @Override
    public long getLong(int columnIndex) {
        return DUMMY_INT;
    }

    @Override
    public double getDouble(int columnIndex) {
        return DUMMY_FLOAT;
    }

}
