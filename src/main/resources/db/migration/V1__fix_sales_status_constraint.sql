ALTER TABLE sales
DROP CONSTRAINT IF EXISTS sales_status_check;

ALTER TABLE sales
ADD CONSTRAINT sales_status_check
CHECK (
  status IN (
    'ACTIVE',
    'OPEN',
    'IN_PROGRESS',
    'CLOSED',
    'COMPLETED',
    'CANCELED'
  )
);
