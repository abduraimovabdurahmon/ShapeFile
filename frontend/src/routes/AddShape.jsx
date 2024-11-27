import React, { useEffect, useState } from "react";
import env from "../../env";
import decodeWindows1251 from "../functions/decodeWindows1251";
import { Link } from "react-router-dom";



const AddShape = () => {
  const [name, setName] = useState("");
  const [ownerName, setOwnerName] = useState("");
  const [file, setFile] = useState(null);
  const [usageTypes, setUsageTypes] = useState([]);
  const [usageType, setUsageType] = useState(null);
  const [regions, setRegions] = useState([]);
  const [region, setRegion] = useState(null);
  const [districts, setDistricts] = useState([]);
  const [district, setDistrict] = useState(null);
  const [villages, setVillages] = useState([]);
  const [village, setVillage] = useState(null);

  const [fetchResponseMessage, setFetchResponseMessage] = useState("");
  const [fetchErrorMessage, setFetchErrorMessage] = useState("");

  async function fetchUsageTypes() {
    const url = env.BASE_URL + "/api/usage-types";
    try {
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        setUsageTypes(data);
      } else {
        console.error("Failed to fetch usage types:", response.statusText);
      }
    } catch (error) {
      console.error("Error fetching usage types:", error.message);
    }
  }

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

  async function uploadFile() {
    try {
      const formData = new FormData();

      formData.append("file", file);
      formData.append("name", name);
      formData.append("ownerName", ownerName);
      formData.append("usageType", usageType);
      formData.append("villageId", village);
      const url = env.BASE_URL + "/api/upload";
      const response = await fetch(url, {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        const data = await response.text();
        setFetchResponseMessage(data);
      } else {
        console.log(response.statusText);
        setFetchErrorMessage(response.statusText);
      }
    } catch (error) {
      console.log(error.message);
      setFetchErrorMessage(
        error.message ? error.message : "Xatolik yuz berdi!"
      );
    }
  }

  //   Handle submit
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file) {
      alert("Fayl tanlanmagan!");
      return;
    }
    if (!name) {
      alert("Shape nomi kiritilmagan!");
      return;
    }
    if (!ownerName) {
      alert("Egasining ismi kiritilmagan!");
      return;
    }
    if (!usageType) {
      alert("Foydalanish turi tanlanmagan!");
      return;
    }
    if (!region) {
      alert("Viloyat tanlanmagan!");
      return;
    }
    if (!district) {
      alert("Tuman tanlanmagan!");
      return;
    }
    if (!village) {
      alert("Qishloq tanlanmagan!");
      return;
    }
    uploadFile();
  };

  useEffect(() => {
    if (fetchErrorMessage || fetchResponseMessage) {
      setTimeout(() => {
        setFetchErrorMessage("");
        setFetchResponseMessage("");
      }, 5000);
    }
  }, [fetchErrorMessage, fetchResponseMessage]);

  useEffect(() => {
    if (district) {
      fetchVillages(district);
    }
    else {
        setVillages([]);
    }
  }, [district]);

  useEffect(() => {
    if (region) {
        fetchDistricts(region);
        setVillages([]);
      }
      else {
        setDistricts([]);
        setVillages([]);
      }
  }, [region]);

  useEffect(() => {
    fetchUsageTypes();
    fetchRegions();
  }, []);

  return (
    <div className="container mt-5">
      {fetchResponseMessage && (
        <div className="alert alert-success" role="alert">
          {fetchResponseMessage}
        </div>
      )}
      {fetchErrorMessage && (
        <div className="alert alert-danger" role="alert">
          {fetchErrorMessage}
        </div>
      )}

      <div className="card shadow-sm">
        <div className="card-body">
          <h1 className="card-title text-center mb-4">Shape qo'shish</h1>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="shapeName">Shape nomi</label>
              <input
                type="text"
                className="form-control"
                id="shapeName"
                onChange={(e) => setName(e.target.value)}
              />
            </div>

            <div className="form-group mt-3">
              <label htmlFor="ownerName">Egasining ismi</label>
              <input
                type="text"
                className="form-control"
                id="ownerName"
                onChange={(e) => setOwnerName(e.target.value)}
              />
            </div>

            <div className="form-group mt-3">
              <label htmlFor="shapeFile">Shape File:   </label>
              <input
                type="file"
                className="form-control-file"
                id="shapeFile"
                accept=".zip"
                onChange={(e) => {
                  setFile(e.target.files[0]);
                }}
              />
            </div>

            <div className="form-group mt-3">
              <label htmlFor="usageType">Foydalanish turi</label>
              <select
                className="form-control"
                id="usageType"
                onChange={(e) => setUsageType(e.target.value)}
              >
                <option value="">Tanlang</option>
                {usageTypes.map((usageType) => (
                  <option key={usageType} value={usageType}>
                    {usageType}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group mt-3">
              <label htmlFor="region">Viloyat</label>
              <select
                className="form-control"
                id="region"
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
              <div className="form-group mt-3">
                <label htmlFor="district">Tuman, shaxar</label>
                <select
                  className="form-control"
                  id="district"
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
              <div className="form-group mt-3">
                <label htmlFor="village">Qishloq</label>
                <select
                  className="form-control"
                  id="village"
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

            <div>
              <button type="submit" className="btn btn-primary btn-block m-2">
                Yuborish
              </button>
              <button type="reset" className="btn btn-secondary btn-block m-2">
                Tozalash
              </button>

              {/* Main */}
              <button type="button" className="btn btn-success btn-block m-2">
                <Link to="/" style={{color: "white"}}>Orqaga</Link>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddShape;
