// 初始化时间
const initDate = (oldDate, full) => {
    // 1. **关键修改：添加对 oldDate 的非空检查**
    if (!oldDate) {
        // 如果 oldDate 是 null, undefined, 空字符串等，则返回空字符串或默认值
        // 建议返回一个默认值，例如 'N/A' 或 '-'，以便在页面上显示更友好
        return '';
    }

    // 确保 oldDate 是字符串类型，以防传入数字或其他非字符串类型
    const dateString = String(oldDate);

    // 假设 oldDate 格式是 "YYYY-MM-DD HH:mm:ss"
    // 使用正则表达式或 split 获取日期和时间部分
    const parts = dateString.split(' ');
    const datePart = parts[0]; // "YYYY-MM-DD"
    const timePart = parts[1]; // "HH:mm:ss" 或 undefined

    const dateSegments = datePart.split('-'); // ["YYYY", "MM", "DD"]

    const year = dateSegments[0];
    const month = dateSegments[1]; // 字符串形式的月份 '01', '02' 等
    const day = dateSegments[2];

    // 可以选择使用 Date 对象进行更复杂的日期操作，但对于仅获取年月日，直接字符串操作也可以
    // var odate = new Date(oldDate); // 这种方式如果 oldDate 格式不标准可能导致 Invalid Date

    if (full === 'all') {
        // 直接使用已分割的日期部分
        return `${year}年${month}月${day}日`;
    } else if (full === 'year') {
        return year;
    } else if (full === 'month') {
        // 返回数字月份，如果需要去除前导零，可以转换为数字
        return parseInt(month, 10);
    } else if (full === 'date') {
        // 返回数字日期，如果需要去除前导零，可以转换为数字
        return parseInt(day, 10);
    } else if (full === 'newDate') {
        // 保持 YYYY年MM月DD日 格式，这里的 month 和 date 已经是带前导零的字符串
        return `${year}年${month}月${day}日`;
    } else {
        // 如果 full 参数不匹配任何已知情况，返回默认的 YYYY-MM-DD 格式
        return datePart;
    }
};

export {
    initDate // 设置时间
};