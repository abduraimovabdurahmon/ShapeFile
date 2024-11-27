function decodeWindows1251(encodedStr) {
    const bytes = new Uint8Array([...encodedStr].map(char => char.charCodeAt(0)));
    const decoder = new TextDecoder('windows-1251');
    return decoder.decode(bytes);
}

export default decodeWindows1251;