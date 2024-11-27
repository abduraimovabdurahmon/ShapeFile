import React, { useEffect, useState } from "react";
import env from "../../env";
import decodeWindows1251 from "../functions/decodeWindows1251";
import { Link, useSearchParams } from "react-router-dom";

const Shapes = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [region, setRegion] = useState(searchParams.get("region") || null);
  const [district, setDistrict] = useState(
    searchParams.get("district") || null
  );
  const [village, setVillage] = useState(searchParams.get("village") || null);
  const [regions, setRegions] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [villages, setVillages] = useState([]);
  const [shapes, setShapes] = useState([]);

  async function fetchRegions() {
    const url = env.BASE_URL + "/api/regions";
    try {
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        setRegions(data);
      } else {
        console.error("Failed to fetch regions:", response.statusText);
      }
    } catch (error) {
      console.error("Error fetching regions:", error.message);
    }
  }

  async function fetchDistricts(regionId) {
    const url = env.BASE_URL + `/api/regions/${regionId}/districts`;
    try {
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        setDistricts(data);
      } else {
        console.error("Failed to fetch districts:", response.statusText);
      }
    } catch (error) {
      console.error("Error fetching districts:", error.message);
    }
  }

  async function fetchVillages(districtId) {
    const url = env.BASE_URL + `/api/districts/${districtId}/villages`;
    try {
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        setVillages(data);
      } else {
        console.error("Failed to fetch villages:", response.statusText);
      }
    } catch (error) {
      console.error("Error fetching villages:", error.message);
    }
  }

  async function fetchShapeByVillage(villageId) {
    const url = env.BASE_URL + `/api/villages/${villageId}/shapes`;
    try {
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        setShapes(data);
      } else {
        console.error("Failed to fetch shapes:", response.statusText);
      }
    } catch (error) {
      console.error("Error fetching shapes:", error.message);
    }
  }

  useEffect(() => {
    if (village) {
      searchParams.set("village", village);
      setSearchParams(searchParams);
      fetchShapeByVillage(village);
    }
  }, [village]);

  useEffect(() => {
    if (district) {
      searchParams.set("district", district);
      setSearchParams(searchParams);
      fetchVillages(district);
    } else {
      setVillages([]);
    }
  }, [district]);

  useEffect(() => {
    if (region) {
      searchParams.set("region", region);
      setSearchParams(searchParams);
      fetchDistricts(region);
      setVillages([]);
    } else {
      setDistricts([]);
      setVillages([]);
    }
  }, [region]);

  useEffect(() => {
    fetchRegions();
  }, []);

  return (
    <>
      <header className="d-flex flex-wrap align-items-center bg-light p-3 rounded">
        {/* Back button */}
        <Link to="/" className="btn btn-primary m-2">
          Orqaga
        </Link>
        <h2 className="mr-3 mb-0">Tanlang:</h2>
        <div className="form-group m-2">
          <select
            className="form-control"
            id="region"
            value={region || ""}
            onChange={(e) => setRegion(e.target.value)}
          >
            <option value="">Tanlang</option>
            {regions.map((region) => (
              <option key={region.id} value={region.id}>
                {decodeWindows1251(region.nameUz)}
              </option>
            ))}
          </select>
        </div>

        {region && districts && (
          <div className="form-group m-2">
            <select
              className="form-control"
              id="district"
              value={district || ""}
              onChange={(e) => setDistrict(e.target.value)}
            >
              <option value="">Tanlang</option>
              {districts.map((district) => (
                <option key={district.id} value={district.id}>
                  {decodeWindows1251(district.nameUz)}
                </option>
              ))}
            </select>
          </div>
        )}

        {district && region && (
          <div className="form-group m-2">
            <select
              className="form-control"
              id="village"
              value={village || ""}
              onChange={(e) => setVillage(e.target.value)}
            >
              <option value="">Tanlang</option>
              {villages.map((village) => (
                <option key={village.id} value={village.id}>
                  {decodeWindows1251(village.nameUz)}
                </option>
              ))}
            </select>
          </div>
        )}
      </header>

      <div className="container">
        {/* <h1>{region + district + village + " dagi barcha shape'lar ro'yxati:"}</h1> */}

        {shapes.length > 0 ? (
          <table className="table table-striped table-bordered mt-3">
            <thead>
              <tr>
                <th scope="col" colSpan="5" className="">
                  <span>
                    {regions
                      .filter((r) => r.id == region)
                      .map((r) => (
                        <span key={r.id}>
                          {decodeWindows1251(r.nameUz)}&nbsp;
                        </span>
                      ))}
                    {districts
                      .filter((d) => d.id == district)
                      .map((d) => (
                        <span key={d.id}>
                          {decodeWindows1251(d.nameUz)}&nbsp;
                        </span>
                      ))}
                    
                    {villages
                      .filter((v) => v.id == village)
                      .map((v) => (
                        <span key={v.id}>
                          {decodeWindows1251(v.nameUz)}&nbsp;
                        </span>
                      ))}
                    dagi barcha shape'lar ro'yxati:
                  </span>
                </th>
              </tr>
              <tr>
                <th scope="col" className="text-secondary">Raqami</th>
                <th scope="col" className="text-secondary">Nomi</th>
                <th scope="col" className="text-secondary">Egasi</th>
                <th scope="col" className="text-secondary">Foydalanish turi</th>
                <th scope="col" className="text-secondary">Funksiyalar</th>
              </tr>
            </thead>
            <tbody>
              {shapes.map((shape) => (
                <tr key={shape.id}>
                  <td>{shape.id}</td>
                  <td>{decodeWindows1251(shape.name)}</td>
                  <td>{decodeWindows1251(shape.owner_name)}</td>
                  <td>
                    {shape.usage_type == "RESIDENTIAL"
                      ? "Axoli yashash joyi"
                      : shape.usage_type == "COMMERCIAL"
                      ? "Tijorat"
                      : shape.usage_type == "AGRICULTURAL"
                      ? "Qishloq xo'jaligi"
                      : shape.usage_type == "INDUSTRIAL"
                      ? "Sanoat"
                      : "Boshqa"}
                  </td>
                  <td>
                    {/* <Link to={`/shape/${shape.id}`} className="btn btn-primary">
                      Xaritada ko'rish
                    </Link> */}
                    <div className="btn-group">
                      <Link
                        to={`/shape/${shape.id}`}
                        className="btn btn-primary btn-sm"
                      >
                        Ko'rish
                      </Link>
                      <button
                        type="button"
                        className="btn btn-danger btn-sm"
                        onClick={async () => {
                          const accept = window.confirm(
                            "Haqiqatdan ham ushbu shape'ni o'chirmoqchimisiz ?"
                          );
                          if (accept) {
                            const response = await fetch(
                              `${env.BASE_URL}/api/shapes/${shape.id}`,
                              {
                                method: "DELETE",
                              }
                            );
                            if (response.ok) {
                              const newShapes = shapes.filter(
                                (s) => s.id !== shape.id
                              );
                              setShapes(newShapes);
                            } else {
                              console.error(
                                "Failed to delete shape:",
                                response.statusText
                              );
                            }
                          }
                        }}
                      >
                        O'chirish
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p
            className="alert alert-warning text-center"
            style={{ marginTop: "20px" }}
          >
            Hech narsa topilmadi!
          </p>
        )}
      </div>
    </>
  );
};

export default Shapes;
