import React from 'react';
import useForm from "react-hook-form";
import {getData} from "../Utils/NetworkUtils";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import {useHistory} from 'react-router-dom';
import {Pet} from "./PetList";
import {Vet} from "./VetList";

/*
* Mudanças no servidor:
* slot date de Date para Long
* no get free slots acrescentei dia e vet para reduzir a lista de busca
*  TODO dar feedback que consulta foi added com sucesso
* */

interface Slot {
    id: string,
    start: number,
    available: boolean
}

type FormData = {
    desc: string;
    vet: string;
    pet: string;
    slot: string
}


const AddAppointmentForm = (props: { currentUserId: number }) => {
    const {register, setValue, handleSubmit, watch, errors} = useForm<FormData>();

    const [vets, setVets] = React.useState([]);
    const [pets, setPets] = React.useState([]);
    const [slots, setSlots] = React.useState([] as Slot[]);
    const [date, setDate] = React.useState(new Date());

    // TODO need to know the id of the current logged in client
    const id = props.currentUserId;

    const postAppointment = (clientID: number, date: Date, desc: string, vet: number, pet: number) => {
        return fetch('/appointments/', {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({clientID: clientID, date: date, desc: desc, id: 0, petID: pet, vetID: vet})
        })
            .then(response => {
                if (response.ok)
                    return response.json();
                else {
                    console.log(`Error: ${response.status}: ${response.statusText}`);
                    console.error(response)
                }
            })
            .catch(reason => {
                console.log(reason);
            });
    }

    let history = useHistory()

    const onSubmit = handleSubmit(({desc, pet, vet, slot}) => {
        console.log(desc, vet, pet, slot);
        slots.map((s) => {
            if (s.id == slot) {
                let d = s.start
                let date = new Date(d)
                postAppointment(id, date, "", +vet, +pet);
            }
        })
        history.push("/")
    });

    const monthNames = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    const handleChange = (d: Date) => {
        setDate(d);
    };

    React.useEffect(() => {
        getData("/vets/", [])
            .then(data => {
                console.log("fetch vets form effect: " + JSON.stringify(data));
                setVets(data);
            });

        if (typeof id != "undefined") {
            getData(`/clients/${id}/pets`, [])
                .then(data => {
                    console.log("fetch client pets form effect: " + JSON.stringify(data));
                    setPets(data);
                })
        }

    }, []);

    React.useEffect(() => {
        let month = monthNames[date.getMonth()].slice(0, 3).toUpperCase();
        let day = date.getUTCDate();
        let id = watch("vet");

        if (id != "") {
            getData(`/vets/${month}/${day}/${id}/freeslots`, [])
                .then(data => {
                    console.log("fetch free slots: " + JSON.stringify(data));
                    setSlots(data);
                });
        }
    }, [date])

    let vetOptionsList = vets.length > 0
        && vets.map((item: Vet, i) => {
            return (
                <option key={i} value={item.id}>{item.username}</option>
            )
        });

    let petOptionsList = pets.length > 0
        && pets.map((item: Pet, i) => {
            return (
                <option key={i} value={item.id}>{item.name}</option>
            )
        });

    let slotOptionsList = slots.length > 0
        && slots.map((item, i) => {
            let d = item.start
            let date = new Date(d)
            console.log(slots.length)
            return (
                <option disabled={!item.available} key={i} value={item.id}>{date.toTimeString().slice(0, 8)}</option>
            )
        });


    return (
        <div className="container mb-5">
            <br/>
            <h1 className="text-center">Create a new Appointment</h1>
            <form onSubmit={onSubmit}>
                {/* <div className="form-group">
                    <label>Description</label>
                    <textarea className="form-control" id="desc" name="desc" ref={register({required: true})}/>
                    {errors.desc && 'A brief description is required.'}
                </div>*/}
                <div className="form-group">
                    <label>Vet</label>
                    <select defaultValue="" className="form-control" id="vet" name="vet"
                            ref={register({required: true})}>
                        <option disabled value=""> -- select an option --</option>
                        {vetOptionsList}
                    </select>
                    {errors.vet && 'You need to choose a vet.'}

                </div>
                <div className="form-group">
                    <label>Pet</label>
                    <select defaultValue="" className="form-control" id="pet" name="pet"
                            ref={register({required: true})}>
                        <option disabled value=""> -- select an option --</option>
                        {petOptionsList}
                    </select>
                    {errors.pet && 'You need to choose a pet.'}
                </div>
                <div className="form-group">
                    <DatePicker
                        selected={date}
                        onChange={handleChange}
                    />
                    <p>you need to choose a date to pick free slot</p>

                </div>
                <div className="form-group">
                    <label>Free Slots</label>
                    <select className="form-control" id="slot" name="slot" ref={register}>
                        {slotOptionsList}
                    </select>
                </div>

                <input className="btn btn-primary float-right" type="submit" value="Create appointment"/>
            </form>
        </div>
    );
};

export default AddAppointmentForm;