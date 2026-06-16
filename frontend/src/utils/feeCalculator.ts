export function calculatePropertyFee(area: number, type: 'property' | 'parking' | 'utilities') {
  const rateMap = {
    property: 3.2,
    parking: 280,
    utilities: 1.15,
  };
  if (type === 'parking') {
    return rateMap.parking;
  }
  return Number((area * rateMap[type]).toFixed(2));
}
