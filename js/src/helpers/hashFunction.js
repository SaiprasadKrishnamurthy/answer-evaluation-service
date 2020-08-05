const stringToHash = (string) => {
    let hash = 0;
    if (string.length === 0) return hash;
    string.split('').forEach((_, idx) => {
        let char = string.charCodeAt(idx);
        hash = ((hash << 5) - hash) + char;
        hash = hash & hash;
    });
    return hash.toString()
}

export default stringToHash