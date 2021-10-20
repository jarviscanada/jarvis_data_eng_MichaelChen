-- 1. Group hosts by hardware info
SELECT
    cpu_number,
    id,
    total_mem
FROM
    host_info
ORDER BY
    cpu_number,
    total_mem desc;

-- Helper function for rounding timestamp to nearest 5 min.
CREATE FUNCTION round5(ts timestamp) RETURNS timestamp AS
$$
BEGIN
    RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$
    LANGUAGE PLPGSQL;

-- 2. Average memory usage
SELECT
    host_id,
    hostname,
    round5(host_usage.timestamp) AS rounded_ts,
    AVG((total_mem - memory_free) * 100 / total_mem):: int AS used_mem
FROM
    host_usage
        INNER JOIN host_info ON host_usage.host_id = host_info.id
GROUP BY
    host_id,
    rounded_ts,
    hostname
ORDER BY
    host_id,
    rounded_ts;

-- 3. Detect host failure
SELECT
    host_id,
    round5(timestamp) AS rounded_ts,
    COUNT(*) AS num_data_points
FROM
    host_usage
GROUP BY
    host_id,
    rounded_ts
HAVING
    count(*) < 3;
