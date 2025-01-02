CREATE TABLE test.test(
    col1 UInt32,
    col2 DateTime64,
    col3 UInt32,
    col4 UInt32
)
ENGINE MergeTree
order by col1;